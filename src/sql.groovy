import groovy.sql.Sql

Map dbConnParams = [
        url     : 'jdbc:clickhouse://localhost:8123/default',
        user    : 'default',
        password: '',
        driver  : 'ru.yandex.clickhouse.ClickHouseDriver']

def sql = Sql.newInstance(dbConnParams)

sql.eachRow("SELECT * FROM `default`.model_tcp_metrics_1m LIMIT 10") { rs ->
    println(rs.date)
    println(rs.appId)
}


sql.close()

