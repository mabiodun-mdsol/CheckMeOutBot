# CheckMeOutBot

## Description ##
The CheckMeOutBot is a helpful tool that gives extra visibility to ensure that pull requests are being managed correctly 


The bot allows Slack Channels to subscribe to the following : 
   * When a PR is closed but still has certain labels
   * When a branch is merged but the branch has not been deleted
   * One person having 2 PRs open
   

    
## Requirements 2 ##
Requirements
   * Scala 2.12.7
   * Sbt 1.2.6

 ```
sbt sbtVersion
sbt scalaVersion
````


##Files
Pre packaged jar file: https://s3.console.aws.amazon.com/s3/buckets/study-management/tools/

##Usage

###Configure Github App



Go to: https://github.com/settings/developers

**under homepage url**
* https://localhost/8080/app_status

**under authorisation callback url**
* https://localhost/8080/github/authenticate




###Configure Slack App

Go to: https://api.slack.com/apps

**under app name**
* Enter "CheckMeOutBot"

**under Development Workspace**
* select "mdsol"

**under authorisation callback url**
* https://localhost/8080/github/authenticate

**under slash command**
* Create new command
* Set "Command field " to "CheckMeOut"
* Set "Request Url to" "https://ocalhost:8080/slack/commands"



## Configuring the Server ##

Configure the server to listen to incoming connections

 ```
 export CHECK_ME_OUT_BOT_GITHUB_CLIENT_ID=<GITHUB APP CLIENT ID>
 export CHECK_ME_OUT_BOT_GITHUB_CLIENT_SECRET=<GITHUB APP CLIENT SECRET>
 export CHECK_ME_OUT_BOT_SLACK_CLIENT_ID=<SLACK APP CLIENT ID >
 export CHECK_ME_OUT_BOT_SLACK_CLIENT_SECRET=<SLACK APP CLIENT SECRET>
 
 export CHECK_ME_OUT_BOT_GITHUB_USERNAME=<GITHUB USERNAME>
 export CHECK_ME_OUT_BOT_GITHUB_PASSWORD=<GITHUB PASSWORD>
````

Download the jar file from the location
Run the jar file with java - jar [java file name]

The bot should now be running on localhost 8080









