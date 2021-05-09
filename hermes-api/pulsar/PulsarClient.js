const Pulsar = require('pulsar-client');
const mode = process.env.NODE_ENV || 'dev'
const config = require(`../config/${mode}.json`)[mode]

const client = new Pulsar.Client({
    serviceUrl: `pulsar://${config.pulsar.host}:${config.pulsar.host}`,
    operationTimeoutSeconds: 30,
  });



module.exports = { client };