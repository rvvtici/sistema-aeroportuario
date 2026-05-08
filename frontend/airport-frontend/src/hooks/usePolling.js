import { useState, useEffect, useCallback } from 'react'

export function usePolling(fetchFn, intervalMs = 5000) {
  const [data, setData] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [lastUpdate, setLastUpdate] = useState(null)

  const fetch = useCallback(async () => {
    try {
      const result = await fetchFn()
      setData(result)
      setError(null)
      setLastUpdate(new Date())
    } catch (err) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }, [fetchFn])

  useEffect(() => {
    fetch()
    const interval = setInterval(fetch, intervalMs)
    return () => clearInterval(interval)
  }, [fetch, intervalMs])

  return { data, loading, error, lastUpdate, refetch: fetch }
}
