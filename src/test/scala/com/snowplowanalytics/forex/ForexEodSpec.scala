/* 
 * Copyright (c) 2013-2014 Snowplow Analytics Ltd. All rights reserved.
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

// Java
import java.math.BigDecimal
import java.math.RoundingMode
// Scala
import scala.collection.JavaConversions._

// Specs2
import org.specs2.mutable.Specification

// Joda time
import org.joda.time._
import org.joda.money._

class ForexEodSpec extends Specification { 
	// run 'export SBT_OPTS=-Dforex.key=[key]' in command line before running tests
	val forexKey =  sys.env("SBT_OPTS").split("=")(1)
	val fx = new Forex(new ForexConfig(forexKey, false))
	val oer = ForexClient.getClient(fx, forexKey)

  val eodDate = new DateTime(2011, 3, 13, 0, 0)
  val gbpEodRate =  fx.rate.to(CurrencyUnit.GBP).eod(eodDate)
  gbpEodRate match {
    case Left(message) => println(message)
    case Right(money)  => 
                          "USD to GBP eod rate [%s]".format(money) should {
                            "be > 0, historicalCache size = [%s]".format(fx.historicalCache.size) in {
                                money.isPositive
                            }
                          }
  }

  val tradeInYenHistorical = fx.convert(10000).to(CurrencyUnit.JPY).eod(eodDate) 
  tradeInYenHistorical match {
    case Left(message) => println(message)
    case Right(money)  => 
                          "convert 10000 USD dollars to Yen in 2011 = [%s]".format(money) should {
                            "be > 10000" in {
                              money.isGreaterThan(Money.of(CurrencyUnit.JPY, 10000, RoundingMode.HALF_EVEN))
                            }
                          }
  }
}