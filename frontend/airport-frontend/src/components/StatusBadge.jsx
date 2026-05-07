import React from 'react'

const STATUS_MAP = {
  PROGRAMADO:  { label: 'Programado',  color: '#58a6ff', bg: '#0d1a2f', border: '#153254' },
  EMBARCANDO:  { label: 'Embarcando',  color: '#3fb950', bg: '#0d1f12', border: '#1a4021' },
  ATRASADO:    { label: 'Atrasado',    color: '#d29922', bg: '#1f1a0d', border: '#3d3010' },
  CANCELADO:   { label: 'Cancelado',   color: '#f85149', bg: '#1f0d0d', border: '#3d1515' },
  CONCLUIDO:   { label: 'Concluído',   color: '#8b949e', bg: '#161b22', border: '#21262d' },
  CHECK_IN:    { label: 'Check-in',    color: '#58a6ff', bg: '#0d1a2f', border: '#153254' },
  DESPACHADA:  { label: 'Despachada',  color: '#bc8cff', bg: '#1a0f2e', border: '#341d5c' },
  EM_VOO:      { label: 'Em voo',      color: '#39d0d8', bg: '#0d1e1f', border: '#1a3d3f' },
  ENTREGUE:    { label: 'Entregue',    color: '#3fb950', bg: '#0d1f12', border: '#1a4021' },
  EXTRAVIADA:  { label: 'Extraviada',  color: '#f85149', bg: '#1f0d0d', border: '#3d1515' },
  AGUARDANDO:  { label: 'Aguardando',  color: '#d29922', bg: '#1f1a0d', border: '#3d3010' },
  EMBARCADO:   { label: 'Embarcado',   color: '#3fb950', bg: '#0d1f12', border: '#1a4021' },
}

export function StatusBadge({ status }) {
  const cfg = STATUS_MAP[status] || { label: status, color: '#8b949e', bg: '#161b22', border: '#21262d' }

  return (
    <span style={{
      display: 'inline-flex',
      alignItems: 'center',
      gap: 5,
      padding: '2px 8px',
      borderRadius: 4,
      fontSize: 11,
      fontWeight: 500,
      letterSpacing: '0.05em',
      textTransform: 'uppercase',
      background: cfg.bg,
      color: cfg.color,
      border: `1px solid ${cfg.border}`,
      fontFamily: 'var(--font-mono)',
    }}>
      <span style={{
        width: 5, height: 5,
        borderRadius: '50%',
        background: cfg.color,
        flexShrink: 0,
      }} />
      {cfg.label}
    </span>
  )
}
