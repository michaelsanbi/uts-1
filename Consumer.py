from enum import auto
import pika
import json

from pika import channel
from pika.exchange_type import ExchangeType


def messageCallback(
    channel,
    method,
    properties,
    body
):
    data = json.loads(body)
    print("[>>] Received from ", method.routing_key, " : ", data)
    channel.basic_ack(delivery_tag=method.delivery_tag)


def main():
    # Data-Data RabbitMQ
    EXCHANGE_NAME = "E-Commerce"
    ROUTING_KEYS = ["checkout", "item"]
    QUEUE_NAMES = ["checkout_queue", "item_queue"]

    # Butuh Koneksi
    connection = pika.BlockingConnection(
        parameters=pika.ConnectionParameters(host="localhost", port=5672)
    )

    # Mengambil channel
    channel = connection.channel()

    # Declare Exchange -> Memberitahu Broker darimana consumer akan menconsume message
    channel.exchange_declare(
        exchange=EXCHANGE_NAME,
        exchange_type=ExchangeType.direct,
        durable=False
    )

    # Declare Queue -> Memberitahu Broker dari queue mana yang akan diconsume
    channel.queue_declare(
        queue=QUEUE_NAMES[0],
        durable=True
    )
    channel.queue_declare(
        queue=QUEUE_NAMES[1],
        durable=True
    )

    # Declare Bind / Link
    channel.queue_bind(
        exchange=EXCHANGE_NAME,
        queue=QUEUE_NAMES[0],
        routing_key=ROUTING_KEYS[0]
    )
    channel.queue_bind(
        exchange=EXCHANGE_NAME,
        queue=QUEUE_NAMES[1],
        routing_key=ROUTING_KEYS[1]
    )

    # Basic Consume
    channel.basic_consume(
        queue=QUEUE_NAMES[0],
        on_message_callback=messageCallback,
        auto_ack=False
    )

    channel.basic_consume(
        queue=QUEUE_NAMES[1],
        on_message_callback=messageCallback,
        auto_ack=False
    )

    print("[STARTED] Waiting for message!")
    channel.start_consuming()


if __name__ == "__main__":
    main()
