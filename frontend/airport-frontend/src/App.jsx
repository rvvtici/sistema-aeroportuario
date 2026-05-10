import React, { useState, useCallback, useMemo } from 'react'
import { api } from './api.js'
import { usePolling } from './hooks/usePolling.js'
import { VooRow } from './components/VooRow.jsx'
import { BagagemCard } from './components/BagagemCard.jsx'
import { useAuth } from './context/AuthContext'
import { ProtectedRoute } from './components/ProtectedRoute.jsx'

const STATUS_FILTROS = ['TODOS', 'PROGRAMADO', 'EMBARCANDO', 'ATRASADO', 'CANCELADO', 'CONCLUIDO']

function Clock() {
  const [time, setTime] = React.useState(new Date())
  React.useEffect(() => {
    const t = setInterval(() => setTime(new Date()), 1000)
    return () => clearInterval(t)
  }, [])
  return (
    <span style={{ fontFamily: 'var(--font-mono)', fontSize: 13, color: 'var(--text-secondary)' }}>
      {time.toLocaleTimeString('pt-BR')}
    </span>
  )
}

export default function App() {
  const { user, logout } = useAuth()
  const [tab, setTab] = useState('voos')
  const [filtroStatus, setFiltroStatus] = useState('TODOS')
  const [busca, setBusca] = useState('')
  const [buscaBagagem, setBuscaBagagem] = useState('')

  const fetchVoos = useCallback(() => api.voos.listar(), [])
  const fetchTickets = useCallback(() => api.tickets.listar(), [])

  const { data: voos, loading: loadingVoos, error: errorVoos, lastUpdate, refetch: refetchVoos } = usePolling(fetchVoos, 5000)
  const { data: tickets, loading: loadingTickets, error: errorTickets, refetch: refetchTickets } = usePolling(fetchTickets, 8000)

  const ticketsComBagagem = useMemo(() => {
    if (!tickets) return []
    return tickets.filter(t => t.possuiBagagem && t.statusEmbarque !== 'CANCELADO' && t.bagagens?.length > 0)
  }, [tickets])

  const ticketsFiltrados = useMemo(() => {
    if (!buscaBagagem) return ticketsComBagagem
    const q = buscaBagagem.toLowerCase()
    return ticketsComBagagem.filter(t => {
      const bagagem = t.bagagens?.[0]
      const passageiro = t.passagem?.passageiro
      const voo = t.passagem?.voo
      const bagagemCodigo = bagagem?.id ? `BAG${String(bagagem.id).padStart(4, '0')}` : ''
      const vooCodigo = voo?.id && voo?.companhiaAerea
        ? voo.companhiaAerea.replace(/\s+/g, '').replace(/[^a-zA-Z]/g, '').substring(0, 2).toUpperCase() + String(voo.id).padStart(4, '0')
        : ''
      return (
        bagagemCodigo.toLowerCase().includes(q) ||
        vooCodigo.toLowerCase().includes(q) ||
        String(bagagem?.peso).includes(q) ||
        passageiro?.nomeCompleto?.toLowerCase().includes(q) ||
        t.passagem?.numeroAssento?.toLowerCase().includes(q) ||
        voo?.origem?.iata?.toLowerCase().includes(q) ||
        voo?.destino?.iata?.toLowerCase().includes(q)
      )     
    })
  }, [ticketsComBagagem, buscaBagagem])

  const voosFiltrados = useMemo(() => {
    if (!voos) return []
    return voos.filter(v => {
      const matchStatus = filtroStatus === 'TODOS' || v.status === filtroStatus
      const matchBusca = !busca ||
        v.companhiaAerea?.toLowerCase().includes(busca.toLowerCase()) ||
        v.origem?.iata?.includes(busca.toUpperCase()) ||
        v.destino?.iata?.includes(busca.toUpperCase()) ||
        String(v.id).includes(busca)
      return matchStatus && matchBusca
    })
  }, [voos, filtroStatus, busca])

  const contadores = useMemo(() => {
    if (!voos) return {}
    return voos.reduce((acc, v) => {
      acc[v.status] = (acc[v.status] || 0) + 1
      return acc
    }, {})
  }, [voos])

  return (
    <ProtectedRoute>  <div style={{ minHeight: '100vh', background: 'var(--bg-primary)' }}>
      {/* Header */}
      <header style={{
        borderBottom: '1px solid var(--border)',
        padding: '0 32px',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'space-between',
        height: 56,
        position: 'sticky',
        top: 0,
        background: 'var(--bg-primary)',
        zIndex: 10,
      }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: 12 }}>
          <span style={{ fontSize: 16, fontFamily: 'var(--font-display)', fontWeight: 700, letterSpacing: '0.05em', color: 'var(--text-primary)' }}>
            ✈ GLIDER
          </span>
          <span style={{ width: 1, height: 20, background: 'var(--border)' }} />
          <nav style={{ display: 'flex', gap: 4 }}>
            {['voos', 'bagagens'].map(t => (
              <button key={t} onClick={() => setTab(t)} style={{
                background: tab === t ? 'var(--bg-card)' : 'transparent',
                border: tab === t ? '1px solid var(--border-accent)' : '1px solid transparent',
                borderRadius: 6,
                color: tab === t ? 'var(--text-primary)' : 'var(--text-muted)',
                cursor: 'pointer',
                fontFamily: 'var(--font-mono)',
                fontSize: 12,
                padding: '4px 12px',
                textTransform: 'uppercase',
                letterSpacing: '0.06em',
              }}>
                {t}
              </button>
            ))}
          </nav>
        </div>

        <div style={{ display: 'flex', alignItems: 'center', gap: 16 }}>
          {lastUpdate && (
            <span style={{ fontSize: 11, color: 'var(--text-muted)' }}>
              Atualizado em {lastUpdate.toLocaleTimeString('pt-BR')}
            </span>
          )}
          <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
            <span style={{
              fontSize: 11,
              color: 'var(--text-muted)',
              fontFamily: 'var(--font-mono)',
            }}>
            </span>
            <span style={{
              fontSize: 10,
              fontFamily: 'var(--font-mono)',
              color: user?.role === 'ADMIN' ? 'var(--amber)' : user?.role === 'ATENDENTE' ? 'var(--green)' : 'var(--blue)',
              background: user?.role === 'ADMIN' ? 'var(--amber-bg)' : user?.role === 'ATENDENTE' ? 'var(--green-bg)' : 'var(--blue-bg)',
              border: `1px solid ${user?.role === 'ADMIN' ? 'var(--amber-border)' : user?.role === 'ATENDENTE' ? 'var(--green-border)' : 'var(--blue-border)'}`,
              borderRadius: 4,
              padding: '2px 6px',
              textTransform: 'uppercase',
              letterSpacing: '0.06em',
            }}>
              {user?.role}
            </span>
            <span style={{ fontSize: 11, color: 'var(--text-muted)' }}>
              {user?.aeroportoIata}
            </span>
            <button onClick={logout} style={{
              background: 'transparent',
              border: '1px solid var(--border)',
              borderRadius: 5,
              color: 'var(--text-muted)',
              cursor: 'pointer',
              fontFamily: 'var(--font-mono)',
              fontSize: 11,
              padding: '3px 10px',
              letterSpacing: '0.04em',
            }}
            onMouseEnter={e => {
              e.currentTarget.style.borderColor = 'var(--red-border)'
              e.currentTarget.style.color = 'var(--red)'
            }}
            onMouseLeave={e => {
              e.currentTarget.style.borderColor = 'var(--border)'
              e.currentTarget.style.color = 'var(--text-muted)'
            }}
            >
              ⏻
            </button>
          </div>
          <Clock />
          <div style={{
            width: 8, height: 8, borderRadius: '50%',
            background: errorVoos ? 'var(--red)' : 'var(--green)',
            boxShadow: errorVoos ? '0 0 6px var(--red)' : '0 0 6px var(--green)',
          }} />
        </div>
      </header>

      <main style={{ padding: '24px 32px', maxWidth: 1400, margin: '0 auto' }}>

        {/* Summary cards */}
        {tab === 'voos' && voos && (
          <div style={{ display: 'flex', gap: 10, marginBottom: 24, flexWrap: 'wrap' }}>
            {[
              { key: 'total',      label: 'Total',       value: voos.length,                color: 'var(--text-primary)' },
              { key: 'PROGRAMADO', label: 'Programados', value: contadores.PROGRAMADO || 0, color: 'var(--blue)' },
              { key: 'EMBARCANDO', label: 'Embarcando',  value: contadores.EMBARCANDO || 0, color: 'var(--amber)' },
              { key: 'ATRASADO',   label: 'Atrasados',   value: contadores.ATRASADO   || 0, color: 'var(--orange)' },
              { key: 'CANCELADO',  label: 'Cancelados',  value: contadores.CANCELADO  || 0, color: 'var(--red)' },
              { key: 'CONCLUIDO', label: 'Concluidos', value: contadores.CONCLUIDO || 0, color: 'var(--green)' },

            ].map(item => (
              <div key={item.key} style={{
                background: 'var(--bg-card)',
                border: '1px solid var(--border)',
                borderRadius: 8,
                padding: '10px 18px',
                minWidth: 110,
              }}>
                <div style={{ fontSize: 10, color: 'var(--text-muted)', textTransform: 'uppercase', letterSpacing: '0.08em', marginBottom: 4 }}>
                  {item.label}
                </div>
                <div style={{ fontSize: 22, fontWeight: 600, color: item.color, fontFamily: 'var(--font-mono)' }}>
                  {item.value}
                </div>
              </div>
            ))}
          </div>
        )}

        {/* Voos tab */}
        {tab === 'voos' && (
          <>
            <div style={{ display: 'flex', gap: 10, marginBottom: 16, alignItems: 'center', flexWrap: 'wrap' }}>
              <input
                placeholder="Buscar voo, companhia, IATA..."
                value={busca}
                onChange={e => setBusca(e.target.value)}
                style={{
                  background: 'var(--bg-card)',
                  border: '1px solid var(--border-accent)',
                  borderRadius: 6,
                  color: 'var(--text-primary)',
                  fontFamily: 'var(--font-mono)',
                  fontSize: 12,
                  padding: '6px 12px',
                  outline: 'none',
                  width: 240,
                }}
              />
              <div style={{ display: 'flex', gap: 4, flexWrap: 'wrap' }}>
                {STATUS_FILTROS.map(s => (
                  <button key={s} onClick={() => setFiltroStatus(s)} style={{
                    background: filtroStatus === s ? 'var(--bg-card-hover)' : 'transparent',
                    border: `1px solid ${filtroStatus === s ? 'var(--border-accent)' : 'var(--border)'}`,
                    borderRadius: 5,
                    color: filtroStatus === s ? 'var(--text-primary)' : 'var(--text-muted)',
                    cursor: 'pointer',
                    fontFamily: 'var(--font-mono)',
                    fontSize: 11,
                    padding: '4px 10px',
                    textTransform: 'uppercase',
                    letterSpacing: '0.05em',
                  }}>
                    {s}
                    {s !== 'TODOS' && contadores[s] ? ` (${contadores[s]})` : ''}
                  </button>
                ))}
              </div>
            </div>

            {loadingVoos ? (
              <div style={{ color: 'var(--text-muted)', padding: 40, textAlign: 'center', fontFamily: 'var(--font-mono)', fontSize: 12 }}>
                <div style={{ marginBottom: 8 }}>⟳ Conectando ao servidor...</div>
                <div style={{ fontSize: 11, color: 'var(--text-muted)', opacity: 0.6 }}>Isso pode levar alguns segundos!</div>
              </div>
            ) : errorVoos ? (
              <div style={{ color: 'var(--red)', padding: 40, textAlign: 'center' }}>
                Erro ao conectar com a API — {errorVoos}
              </div>
            ) : (
              <div style={{
                background: 'var(--bg-card)',
                border: '1px solid var(--border)',
                borderRadius: 10,
                overflow: 'hidden',
              }}>
                <table style={{ width: '100%', borderCollapse: 'collapse' }}>
                  <thead>
                    <tr style={{ borderBottom: '1px solid var(--border)' }}>
                      {['Código de Voo', 'Companhia', 'Rota / Aeronave', 'Horários', 'Terminal', 'Portão', 'Status'].map(h => (
                        <th key={h} style={{
                          padding: '10px 16px',
                          textAlign: 'left',
                          fontSize: 10,
                          color: 'var(--text-muted)',
                          textTransform: 'uppercase',
                          letterSpacing: '0.1em',
                          fontWeight: 500,
                          background: 'var(--bg-secondary)',
                        }}>
                          {h}
                        </th>
                      ))}
                    </tr>
                  </thead>
                  <tbody>
                    {voosFiltrados.length === 0 ? (
                      <tr>
                        <td colSpan={7} style={{ padding: 40, textAlign: 'center', color: 'var(--text-muted)' }}>
                          Nenhum voo encontrado.
                        </td>
                      </tr>
                    ) : (
                      voosFiltrados.map(voo => (
                        <VooRow key={voo.id} voo={voo} onUpdate={refetchVoos} user={user}/>
                      ))
                    )}
                  </tbody>
                </table>
              </div>
            )}
          </>
        )}

        {/* Bagagens tab */}
        {tab === 'bagagens' && (
          <>
            <div style={{ display: 'flex', gap: 25, marginBottom: 16, alignItems: 'center', flexWrap: 'wrap' }}>
              <span style={{ fontSize: 11, color: 'var(--text-muted)', textTransform: 'uppercase', letterSpacing: '0.08em' }}>
                {ticketsFiltrados.length} bagagens ativas
              </span>
              <input
                placeholder="Buscar por id, peso, passageiro, assento, rota..."
                value={buscaBagagem}
                onChange={e => setBuscaBagagem(e.target.value)}
                style={{
                  background: 'var(--bg-card)',
                  border: '1px solid var(--border-accent)',
                  borderRadius: 9,
                  color: 'var(--text-primary)',
                  fontFamily: 'var(--font-mono)',
                  fontSize: 12,
                  padding: '6px 12px',
                  outline: 'none',
                  width: 350,
                }}
              />
            </div>

            {loadingTickets ? (
              <div style={{ color: 'var(--text-muted)', padding: 40, textAlign: 'center' }}>
                Carregando bagagens...
              </div>
            ) : errorTickets ? (
              <div style={{ color: 'var(--red)', padding: 40, textAlign: 'center' }}>
                erro ao conectar com a API — {errorTickets}
              </div>
            ) : (
              <div style={{
                display: 'grid',
                gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))',
                gap: 10,
              }}>
                {ticketsFiltrados.length === 0 ? (
                  <div style={{ color: 'var(--text-muted)', padding: 40 }}>
                    Nenhuma bagagem registrada.
                  </div>
                ) : (
                  ticketsFiltrados.map(ticket => (
                    <BagagemCard key={ticket.id} ticket={ticket} user={user} onDelete={refetchTickets}/>
                  ))
                )}
              </div>
            )}
          </>
        )}
      </main>
    </div>
  </ProtectedRoute>
  )
}
