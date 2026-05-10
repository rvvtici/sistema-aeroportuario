import { useState, useEffect, useCallback, useRef } from 'react'

export function usePolling(fetchFn, intervalMs = 5000) {
  const [data, setData] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [lastUpdate, setLastUpdate] = useState(null)
  const jaCarregou = useRef(false)

  const fetch = useCallback(async () => {
    try {
      const result = await fetchFn()
      if (result !== null && result !== undefined) {
        setData(result)
        jaCarregou.current = true
      }
      setError(null)
      setLastUpdate(new Date())
    } catch (err) {
      if (jaCarregou.current) {
        // já tinha dado certo antes — mostra erro de reconexão
        setError(err.message)
      }
      // primeira carga — mantém loading, não mostra erro
    } finally {
      if (jaCarregou.current) {
        setLoading(false)
      }
    }
  }, [fetchFn])

  useEffect(() => {
    fetch()
    const interval = setInterval(fetch, intervalMs)
    return () => clearInterval(interval)
  }, [fetch, intervalMs])

  return { data, loading, error, lastUpdate, refetch: fetch }
}