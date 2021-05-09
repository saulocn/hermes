const Pulsar = require('pulsar-client');
const PULSAR_HOST = 'localhost'
const PULSAR_PORT = '6650'

const client = new Pulsar.Client({
    serviceUrl: `pulsar://${PULSAR_HOST}:${PULSAR_PORT}`,
    operationTimeoutSeconds: 30,
  });



module.exports = { client };