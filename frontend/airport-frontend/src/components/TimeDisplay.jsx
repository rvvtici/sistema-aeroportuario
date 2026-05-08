import React from 'react'

function fmt(dt) {
  if (!dt) return '—'
  const d = new Date(dt)
  return d.toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' })
}

function fmtDate(dt) {
  if (!dt) return ''
  const d = new Date(dt)
  return d.toLocaleDateString('pt-BR', { day: '2-digit', month: '2-digit' })
}

function isDelayed(original, previsto) {
  if (!original || !previsto) return false
  return new Date(previsto) > new Date(original)
}

export function TimeDisplay({ label, original, previsto }) {
  const delayed = isDelayed(original, previsto)
  const origDate = original ? fmtDate(original) : ''
  const prevDate = previsto ? fmtDate(previsto) : ''
  const showPrevDate = prevDate && prevDate !== origDate

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
      <span style={{ fontSize: 10, color: 'var(--text-muted)', textTransform: 'uppercase', letterSpacing: '0.08em' }}>
        {label}
      </span>
      <div style={{ display: 'flex', alignItems: 'center', gap: 6 }}>
        <span style={{
          fontFamily: 'var(--font-mono)',
          fontSize: 15,
          fontWeight: 500,
          color: delayed ? 'var(--text-muted)' : 'var(--text-primary)',
          textDecoration: delayed ? 'line-through' : 'none',
        }}>
          {fmt(original)}
          {origDate && <span style={{ fontSize: 10, marginLeft: 3, opacity: 0.5 }}>{origDate}</span>}
        </span>
        {delayed && previsto && (
          <span style={{
            fontFamily: 'var(--font-mono)',
            fontSize: 15,
            fontWeight: 500,
            color: '#d29922',
          }}>
            {fmt(previsto)}
            {showPrevDate && <span style={{ fontSize: 10, marginLeft: 3, opacity: 0.7 }}>{prevDate}</span>}
          </span>
        )}
      </div>
    </div>
  )
}
