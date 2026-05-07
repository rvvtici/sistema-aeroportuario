CREATE KEYSPACE IF NOT EXISTS airport_logs
  WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};
