import { useState } from 'react'
import { useAuth } from '../context/AuthContext'

export function LoginPage() {
  const { login } = useAuth()
  const [loginVal, setLoginVal] = useState('')
  const [senha, setSenha] = useState('')
  const [erro, setErro] = useState('')
  const [loading, setLoading] = useState(false)

  async function handleSubmit() {
    setErro('')
    setLoading(true)
    try {
      const res = await fetch('http://localhost:8080/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ login: loginVal, senha }),
      })
      if (!res.ok) {
        const msg = await res.text()
        setErro(msg || 'Credenciais inválidas')
        return
      }
      const data = await res.json()
      login(data)
    } catch {
      setErro('Erro ao conectar com o servidor')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div style={{
      minHeight: '100vh',
      background: 'var(--bg-primary)',
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'center',
    }}>
      <div style={{
        background: 'var(--bg-card)',
        border: '1px solid var(--border)',
        borderRadius: 12,
        padding: '40px 36px',
        width: 340,
      }}>
        <div style={{ marginBottom: 32, textAlign: 'center' }}>
          <div style={{ fontSize: 22, fontWeight: 700, fontFamily: 'var(--font-display)', letterSpacing: '0.05em', color: 'var(--text-primary)', marginBottom: 6 }}>
            ✈ GLIDER
          </div>
          <div style={{ fontSize: 12, color: 'var(--text-muted)', fontFamily: 'var(--font-mono)' }}>
            Sistema de Gestão Aeroportuária
          </div>
        </div>

        <div style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
          <input
            placeholder="Login"
            value={loginVal}
            onChange={e => setLoginVal(e.target.value)}
            style={inputStyle}
          />
          <input
            type="password"
            placeholder="Senha"
            value={senha}
            onChange={e => setSenha(e.target.value)}
            onKeyDown={e => e.key === 'Enter' && handleSubmit()}
            style={inputStyle}
          />

          {erro && (
            <div style={{ fontSize: 12, color: 'var(--red)', fontFamily: 'var(--font-mono)', textAlign: 'center' }}>
              {erro}
            </div>
          )}

          <button
            onClick={handleSubmit}
            disabled={loading || !loginVal || !senha}
            style={{
              marginTop: 8,
              background: loading ? 'var(--bg-secondary)' : 'var(--bg-card-hover)',
              border: '1px solid var(--border-accent)',
              borderRadius: 6,
              color: 'var(--text-primary)',
              cursor: loading ? 'not-allowed' : 'pointer',
              fontFamily: 'var(--font-mono)',
              fontSize: 12,
              padding: '8px 0',
              textTransform: 'uppercase',
              letterSpacing: '0.08em',
              opacity: !loginVal || !senha ? 0.5 : 1,
            }}
          >
            {loading ? 'autenticando...' : 'entrar'}
          </button>
        </div>
      </div>
    </div>
  )
}

const inputStyle = {
  background: 'var(--bg-secondary)',
  border: '1px solid var(--border-accent)',
  borderRadius: 6,
  color: 'var(--text-primary)',
  fontFamily: 'var(--font-mono)',
  fontSize: 12,
  padding: '8px 12px',
  outline: 'none',
  width: '100%',
  boxSizing: 'border-box',
}