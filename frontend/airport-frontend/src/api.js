const BASE = '/api'

function getToken() {
  // busca o token do contexto — como usamos só memória, precisamos de outro jeito
  // vamos usar uma variável módulo que o AuthContext atualiza
  return window.__glider_token__ || null
}

async function get(path) {
  const res = await fetch(`${BASE}${path}`, {
    headers: getToken() ? { Authorization: `Bearer ${getToken()}` } : {}
  })
  if (!res.ok) throw new Error(`HTTP ${res.status}`)
  return res.json()
}

async function patch(path, params = {}) {
  const query = new URLSearchParams(params).toString()
  const res = await fetch(`${BASE}${path}?${query}`, {
    method: 'PATCH',
    headers: getToken() ? { Authorization: `Bearer ${getToken()}` } : {}
  })
  if (!res.ok) throw new Error(`HTTP ${res.status}`)
  return res.json().catch(() => null)
}

export const api = {
  voos: {
    listar: () => get('/voos'),
    buscar: (id) => get(`/voos/${id}`),
    status: (id) => get(`/status/voos/${id}`),
    atualizarStatus: (id, valor) => patch(`/status/voos/${id}/status`, { valor }),
    atualizarPortao: (id, valor) => patch(`/status/voos/${id}/portao`, { valor }),
  },
  bagagens: {
    status: (id) => get(`/status/bagagens/${id}`),
    atualizarStatus: (id, valor) => patch(`/status/bagagens/${id}/status`, { valor }),
  },
  tickets: {
    listar: () => get('/tickets'),
  },
}