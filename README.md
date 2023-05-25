# Telegram Serbian News Review Bot Application

This is a Java application that scrapes Serbian news from the [Danas.rs](https://danas.rs) website, translates them using Google Translate, and sends the translated news to a Telegram channel.

## Description
TgSerbianNewsBot works by first scraping Serbian news articles from Danas.rs. It filters the articles and adds them to a MongoDB database, then translates the articles using Google Translate. After translation, the articles are divided into four categories: politics, economics, society, and other. Finally, the application sends the translated articles to a Telegram channel.

The application uses several services:

- `DanasService` for scraping the news.
- `MongoService` for filtering and storing news.
- `GoogleTranslateService` for translating news.
- `TelegramSenderService` for sending news to a Telegram channel.

## Installation
Before you start, make sure you have a suitable Java Development Kit (JDK) installed on your machine. JDK 11 or later is recommended.

1. Clone this repository:
```bash
git clone https://github.com/dnfesenk/tg-serbian-news-bot.git
```

2. Navigate into the project directory:
```bash
cd tg-serbian-news-bot
```

3. Build the project with Maven:
```bash
mvn clean install
```

## Usage
Usage
Before you can run the application, you'll need to set up a few environment variables:

- `GOOGLE_APPLICATION_CREDENTIALS_BASE64`: Base64 encoded Google Credentials file for using Google Translate API.
- `MONGO_CONNECTION_STRING`: Connection string of your MongoDB instance.
- `TG_BOT_TOKEN`: Token of your Telegram bot
- `TG_BOT_CHANNEL_ID`: ID of the Telegram channel where the news should be sent.

To run the application, execute:
```bash
java -cp ./target/tg-serbian-news-bot-1.0-SNAPSHOT-jar-with-dependencies.jar com.denisfesenko.TgSerbianNewsBot
```

The application will then start scraping, translating, categorizing, and sending Serbian news articles to your Telegram channel.

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

## License
Telegram Serbian News Review Bot Application is licensed under the MIT License. See the LICENSE file for details.