import React from 'react'

const STATUS_MAP = {
  PROGRAMADO: {
    label: 'Programado',
    color: 'var(--blue)',
    bg: 'var(--blue-bg)',
    border: 'var(--blue-border)',
  },

  EMBARCANDO: {
    label: 'Embarcando',
    color: 'var(--amber)',
    bg: 'var(--amber-bg)',
    border: 'var(--amber-border)',
  },

  ATRASADO: {
    label: 'Atrasado',
    color: 'var(--orange)',
    bg: 'var(--orange-bg)',
    border: 'var(--orange-border)',
  },

  CANCELADO: {
    label: 'Cancelado',
    color: 'var(--red)',
    bg: 'var(--red-bg)',
    border: 'var(--red-border)',
  },

  CONCLUIDO: {
    label: 'Concluído',
    color: 'var(--green)',
    bg: 'var(--green-bg)',
    border: 'var(--green-border)',
  },

  CHECK_IN: {
    label: 'Check-in',
    color: 'var(--blue)',
    bg: 'var(--blue-bg)',
    border: 'var(--blue-border)',
  },

  DESPACHADA: {
    label: 'Despachada',
    color: 'var(--purple)',
    bg: 'var(--purple-bg)',
    border: 'var(--purple-border)',
  },

  TRIAGEM: {
    label: 'Triagem',
    color: 'var(--amber)',
    bg: 'var(--amber-bg)',
    border: 'var(--amber-border)',
  },

  EMBARCADA: {
    label: 'Embarcada',
    color: 'var(--cyan)',
    bg: 'var(--cyan-bg)',
    border: 'var(--cyan-border)',
  },

  DESEMBARCADA: {
    label: 'Desembarcada',
    color: 'var(--green)',
    bg: 'var(--green-bg)',
    border: 'var(--green-border)',
  },

  ESTEIRA: {
    label: 'Na esteira',
    color: 'var(--orange)',
    bg: 'var(--orange-bg)',
    border: 'var(--orange-border)',
  },

  RETIRADA: {
    label: 'Retirada',
    color: 'var(--green)',
    bg: 'var(--green-bg)',
    border: 'var(--green-border)',
  },

  EXTRAVIADA: {
    label: 'Extraviada',
    color: 'var(--red)',
    bg: 'var(--red-bg)',
    border: 'var(--red-border)',
  },

  RETIDA: {
    label: 'Retida',
    color: 'var(--orange)',
    bg: 'var(--orange-bg)',
    border: 'var(--orange-border)',
  },
}

export function StatusBadge({ status }) {
  const cfg = STATUS_MAP[status] || {
  label: status || 'DESCONHECIDO',
  color: 'var(--text-muted)',
  bg: 'var(--bg-secondary)',
  border: 'var(--border)',
}
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
