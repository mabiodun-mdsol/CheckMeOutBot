package com.mdsol.checkmeoutbot

import com.mdsol.checkmeoutbot.app.CMOBServer
import com.mdsol.checkmeoutbot.app.services.AuthorisationService


object Main extends CMOBServer with App with AuthorisationService {
}
