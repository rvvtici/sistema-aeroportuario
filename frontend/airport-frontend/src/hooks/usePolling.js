import { useState, useEffect, useCallback, useRef } from 'react'

export function usePolling(fetchFn, intervalMs = 5000) {
  const [data, setData] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [lastUpdate, setLastUpdate] = useState(null)

  const primeiraCarga = useRef(true)

  const fetch = useCallback(async () => {
    try {
      const result = await fetchFn()

      if (result !== null && result !== undefined) {
        setData(result)
      }

      setError(null)
      setLastUpdate(new Date())

      // só encerra loading após primeiro sucesso
      if (primeiraCarga.current) {
        primeiraCarga.current = false
        setLoading(false)
      }

    } catch (err) {

      // só mostra erro depois que já carregou ao menos uma vez
      if (!primeiraCarga.current) {
        setError(err.message)
      }

    }
  }, [fetchFn])

  useEffect(() => {
    fetch()

    const interval = setInterval(fetch, intervalMs)

    return () => clearInterval(interval)
  }, [fetch, intervalMs])

  return {
    data,
    loading,
    error,
    lastUpdate,
    refetch: fetch
  }
}