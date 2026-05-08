import React, { useState, useCallback } from 'react'
import { StatusBadge } from './StatusBadge.jsx'
import { usePolling } from '../hooks/usePolling.js'
import { api } from '../api.js'

const STATUS_BAGAGEM = [
  'CHECK_IN',
  'DESPACHADA',
  'TRIAGEM',
  'EMBARCADA',
  'DESEMBARCADA',
  'ESTEIRA',
  'RETIRADA',
  'EXTRAVIADA',
  'RETIDA'
]
export function BagagemCard({ ticket }) {
  const bagagem = ticket.bagagens?.[0]
  const bagagemId = bagagem?.id  
  const [editing, setEditing] = useState(false)
  const [tempStatus, setTempStatus] = useState('')
  const [saving, setSaving] = useState(false)

  const fetchStatus = useCallback(() => {
    if (!bagagemId) return null
    return api.bagagens.status(bagagemId)
  }, [bagagemId])
  const { data: statusData, refetch } = usePolling(fetchStatus, 6000)

  const statusAtual = statusData?.status ||  bagagem?.status ||  '—'

  async function salvar() {
    setSaving(true)
    try {
      await api.bagagens.atualizarStatus(bagagemId, tempStatus)
      refetch()
    } catch (e) {
      console.error(e)
    } finally {
      setSaving(false)
      setEditing(false)
    }
  }

  const passageiro = ticket.passagem?.passageiro
  const voo = ticket.passagem?.voo

  return (
    <div style={{
      background: 'var(--bg-card)',
      border: '1px solid var(--border)',
      borderRadius: 8,
      padding: '12px 14px',
      display: 'flex',
      flexDirection: 'column',
      gap: 8,
    }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
        <div>
          <div style={{ fontSize: 11, color: 'var(--text-muted)', textTransform: 'uppercase', letterSpacing: '0.08em' }}>
            Ticket #{bagagemId}
          </div>
          <div style={{ fontSize: 13, fontWeight: 500, marginTop: 2 }}>
            {passageiro?.nomeCompleto || '—'}
          </div>
        </div>
        <div style={{ fontSize: 11, color: 'var(--text-secondary)' }}>
          {voo ? `${voo.origem?.iata} → ${voo.destino?.iata}` : '—'}
        </div>
      </div>

      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
        {editing ? (
          <div style={{ display: 'flex', gap: 4, alignItems: 'center' }}>
            <select
              value={tempStatus}
              onChange={e => setTempStatus(e.target.value)}
              style={selectStyle}
            >
              {STATUS_BAGAGEM.map(s => (
                <option key={s} value={s}>{s}</option>
              ))}
            </select>
            <button onClick={salvar} disabled={saving} style={btnSave}>✓</button>
            <button onClick={() => setEditing(false)} style={btnCancel}>✕</button>
          </div>
        ) : (
          <div
            style={{ display: 'flex', alignItems: 'center', gap: 6, cursor: 'pointer' }}
            onClick={() => { setTempStatus(statusAtual); setEditing(true) }}
            title="Clique para atualizar"
          >
            <StatusBadge status={statusAtual} />
            <span style={{ fontSize: 10, color: 'var(--text-muted)' }}>✎</span>
          </div>
        )}

        <span style={{ fontSize: 11, color: 'var(--text-muted)' }}>
          Assento {ticket.passagem?.numeroAssento || '—'}
        </span>
      </div>
    </div>
  )
}

const selectStyle = {
  background: 'var(--bg-primary)',
  border: '1px solid var(--border-accent)',
  borderRadius: 4,
  color: 'var(--text-primary)',
  fontFamily: 'var(--font-mono)',
  fontSize: 12,
  padding: '3px 6px',
  outline: 'none',
}

const btnSave = {
  background: 'var(--green-bg)',
  border: '1px solid var(--green-border)',
  borderRadius: 4,
  color: 'var(--green)',
  cursor: 'pointer',
  padding: '3px 7px',
  fontSize: 12,
}

const btnCancel = {
  background: 'var(--red-bg)',
  border: '1px solid var(--red-border)',
  borderRadius: 4,
  color: 'var(--red)',
  cursor: 'pointer',
  padding: '3px 7px',
  fontSize: 12,
}
