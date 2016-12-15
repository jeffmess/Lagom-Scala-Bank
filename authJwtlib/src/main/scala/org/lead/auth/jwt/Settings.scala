package org.lead.auth.jwt

import com.typesafe.config.ConfigFactory

/**
  * Created by fsznajderman on 19/11/2016.
  */
object Settings {


  val conf = ConfigFactory.defaultReference()

  val secretkey = conf.getString("secretkey")

}
