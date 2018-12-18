package com.mdsol.checkmeoutbot

import com.mdsol.checkmeoutbot.app.CMOBServer
import com.mdsol.checkmeoutbot.app.services.GithubServices


object Main extends CMOBServer with App with GithubServices {
}
