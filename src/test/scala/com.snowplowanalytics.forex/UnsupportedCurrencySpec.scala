/* 
 * Copyright (c) 2013-2015 Snowplow Analytics Ltd. All rights reserved.
 *
 * This program is licensed to you under the Apache License Version 2.0,
 * and you may not use this file except in compliance with the Apache License Version 2.0.
 * You may obtain a copy of the Apache License Version 2.0 at http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the Apache License Version 2.0 is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Apache License Version 2.0 for the specific language governing permissions and limitations there under.
 */
package com.snowplowanalytics.forex

// Specs2
import org.specs2.mutable.Specification
// TestHelpers
import TestHelpers._
// oerclient
import oerclient.IllegalCurrency
import oerclient.OerResponseError
// Joda money
import org.joda.money.Money

/** 
 *  Testing for unsupported currencies in joda money, e.g. bitcoin(BTC)
 */
class UnsupportedCurrencySpec extends Specification { 
  "Joda money" should {
    Seq("BTC", "RRU", "EEK") foreach { currency =>
      (" not support currency: " + currency) >> {
        val rate = fx.rate(currency).to("GBP").now
        rate must beLike {
          case Left(OerResponseError(_, IllegalCurrency)) => ok
        }
      } 
    }
  }
  
}
