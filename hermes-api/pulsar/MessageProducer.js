const mode = process.env.NODE_ENV || 'dev'
const config = require(`../config/${mode}.json`)[mode]
const client = require('./PulsarClient').client

const sendMessage = async (message) => {
    const producer = await client.createProducer({
        topic: `${config.pulsar.topic}`,
        sendTimeoutMs: 30000,
        batchingEnabled: true,
    });

    producer.send({
        data: Buffer.from(JSON.stringify(message)),
    });
    await producer.flush();
}



module.exports = { sendMessage }

