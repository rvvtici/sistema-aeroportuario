import React, { useState } from 'react'
import { StatusBadge } from './StatusBadge.jsx'
import { TimeDisplay } from './TimeDisplay.jsx'
import { api } from '../api.js'

export function VooRow({ voo, onUpdate, user }) {
  const [editingStatus, setEditingStatus] = useState(false)
  const [editingPortao, setEditingPortao] = useState(false)
  const [tempStatus, setTempStatus] = useState(voo.status)
  const [tempPortao, setTempPortao] = useState(voo.portao || '')
  const [saving, setSaving] = useState(false)

  const statusOptions = ['PROGRAMADO', 'EMBARCANDO', 'ATRASADO', 'CANCELADO', 'CONCLUIDO']

  async function salvarStatus() {
    setSaving(true)
    try {
      await api.voos.atualizarStatus(voo.id, tempStatus)
      onUpdate()
    } catch (e) {
      console.error(e)
    } finally {
      setSaving(false)
      setEditingStatus(false)
    }
  }

  async function salvarPortao() {
    setSaving(true)
    try {
      await api.voos.atualizarPortao(voo.id, tempPortao)
      onUpdate()
    } catch (e) {
      console.error(e)
    } finally {
      setSaving(false)
      setEditingPortao(false)
    }
  }

  function gerarCodigoVoo(companhia, id) {
    if (!companhia) return `#${id}`
    const sigla = companhia
      .replace(/\s+/g, '')        // remove espaços
      .replace(/[^a-zA-Z]/g, '')  // só letras
      .substring(0, 2)
      .toUpperCase()
    const numero = String(id).padStart(4, '0')
    return `${sigla}${numero}`
  }
  const origem = voo.origem?.iata || '—'
  const destino = voo.destino?.iata || '—'

  return (
    <tr
      style={{ borderBottom: '1px solid var(--border)', transition: 'background 0.15s' }}
      onMouseEnter={e => e.currentTarget.style.background = 'var(--bg-card-hover)'}
      onMouseLeave={e => e.currentTarget.style.background = 'transparent'}
    >
      <td style={td}>
        <span style={{ fontWeight: 500, color: 'var(--text-primary)', fontFamily: 'var(--font-mono)', letterSpacing: '0.05em' }}>
          {gerarCodigoVoo(voo.companhiaAerea, voo.id)}
        </span>
      </td>

      <td style={td}>
        <span style={{ fontSize: 12, color: 'var(--text-secondary)' }}>{voo.companhiaAerea}</span>
      </td>

      <td style={td}>
        <div style={{ display: 'flex', alignItems: 'center', gap: 6 }}>
          <span style={{ fontWeight: 500 }}>{origem}</span>
          <span style={{ color: 'var(--text-muted)', fontSize: 12 }}>→</span>
          <span style={{ fontWeight: 500 }}>{destino}</span>
        </div>
        <div style={{ fontSize: 11, color: 'var(--text-muted)', marginTop: 1 }}>
          {voo.aeronave}
        </div>
      </td>

      <td style={td}>
        <div style={{ display: 'flex', flexDirection: 'column', gap: 6 }}>
          <TimeDisplay label="Partida" original={voo.horarioPartida} previsto={voo.previsaoPartida} />
          <TimeDisplay label="Chegada" original={voo.horarioChegada} previsto={voo.previsaoChegada} />
        </div>
      </td>

      <td style={td}>
        <div style={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
          <span style={{ fontSize: 10, color: 'var(--text-muted)', textTransform: 'uppercase', letterSpacing: '0.08em' }}>Terminal</span>
          <span style={{ fontWeight: 500 }}>{voo.terminal || '—'}</span>
        </div>
      </td>

      <td style={td}>
        {user?.role !== 'ATENDENTE' ? (
          editingPortao ? (
            <div style={{ display: 'flex', gap: 4, alignItems: 'center' }}>
              <input
                value={tempPortao}
                onChange={e => setTempPortao(e.target.value)}
                style={inputStyle}
                onKeyDown={e => e.key === 'Enter' && salvarPortao()}
                autoFocus
              />
              <button onClick={salvarPortao} disabled={saving} style={btnSave}>✓</button>
              <button onClick={() => setEditingPortao(false)} style={btnCancel}>✕</button>
            </div>
          ) : (
            <div
              onClick={() => setEditingPortao(true)}
              title="Clique para editar"
              style={{ display: 'flex', flexDirection: 'column', gap: 2, cursor: 'pointer' }}
            >
              <span style={{ fontSize: 10, color: 'var(--text-muted)', textTransform: 'uppercase', letterSpacing: '0.08em' }}>Portão</span>
              <span style={{ fontWeight: 500, color: voo.portao ? 'var(--text-primary)' : 'var(--text-muted)' }}>
                {voo.portao || '—'}
                <span style={{ fontSize: 10, color: 'var(--text-muted)', marginLeft: 4 }}>✎</span>
              </span>
            </div>
          )
        ) : (
          <div style={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
            <span style={{ fontSize: 10, color: 'var(--text-muted)', textTransform: 'uppercase', letterSpacing: '0.08em' }}>Portão</span>
            <span style={{ fontWeight: 500 }}>{voo.portao || '—'}</span>
          </div>
        )}
      </td>

      <td style={td}>
        {user?.role !== 'ATENDENTE' ? (
          editingStatus ? (
            <div style={{ display: 'flex', gap: 4, alignItems: 'center' }}>
              <select
                value={tempStatus}
                onChange={e => setTempStatus(e.target.value)}
                style={selectStyle}
              >
                {statusOptions.map(s => (
                  <option key={s} value={s}>{s}</option>
                ))}
              </select>
              <button onClick={salvarStatus} disabled={saving} style={btnSave}>✓</button>
              <button onClick={() => setEditingStatus(false)} style={btnCancel}>✕</button>
            </div>
          ) : (
            <div onClick={() => setEditingStatus(true)} style={{ cursor: 'pointer' }} title="Clique para editar">
              <StatusBadge status={voo.status} />
              <span style={{ fontSize: 10, color: 'var(--text-muted)', marginLeft: 4 }}>✎</span>
            </div>
          )
        ) : (
          <StatusBadge status={voo.status} />
        )}
      </td>
    </tr>
  )
}
     
const td = {
  padding: '12px 16px',
  verticalAlign: 'middle',
}

const inputStyle = {
  background: 'var(--bg-primary)',
  border: '1px solid var(--border-accent)',
  borderRadius: 4,
  color: 'var(--text-primary)',
  fontFamily: 'var(--font-mono)',
  fontSize: 13,
  padding: '3px 6px',
  width: 60,
  outline: 'none',
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

