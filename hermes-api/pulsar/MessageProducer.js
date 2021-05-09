const client = require('./PulsarClient').client;
const TOPIC_NAME = 'message-topic'


const sendMessage = async (message) => {
    const producer = await client.createProducer({
        topic: `persistent://public/default/${TOPIC_NAME}`,
        sendTimeoutMs: 30000,
        batchingEnabled: true,
    });

    producer.send({
        data: Buffer.from(JSON.stringify(message)),
    });
    await producer.flush();
}



module.exports = { sendMessage }

