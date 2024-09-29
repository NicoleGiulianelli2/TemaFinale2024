/* Generated by AN DISI Unibo */ 
package it.unibo.wis

import it.unibo.kactor.*
import alice.tuprolog.*
import unibo.basicomm23.*
import unibo.basicomm23.interfaces.*
import unibo.basicomm23.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import it.unibo.kactor.sysUtil.createActor   //Sept2023

//User imports JAN2024

class Wis ( name: String, scope: CoroutineScope, isconfined: Boolean=false  ) : ActorBasicFsm( name, scope, confined=isconfined ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		//val interruptedStateTransitions = mutableListOf<Transition>()
		
				var RobPos = ""
				var RP_w = 0
				var Ash_distance = 1000
				var Inc_stat = "off"
				val Ash_limit = 200  //qualche misura
				var rob = "idle" 
				
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						CommUtils.outred("WIS: wis starting...")
						delay(2000) 
						observeResource("localhost","8080","ctxtest","oprobot","info")
						forward("activation_command", "activation_command(start)" ,"incinerator" ) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="wait_message", cond=doswitch() )
				}	 
				state("wait_message") { //this:State
					action { //it:State
						CommUtils.outgreen("WIS: waiting the state update...")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t00",targetState="manageState",cond=whenDispatch("info"))
				}	 
				state("manageState") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("info(X,Y)"), Term.createTerm("info(X,Y)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 RobPos = payloadArg(1)   
						}
						request("pollingRP", "pollingRP(N)" ,"wastestorage" )  
						request("pollingAsh", "pollingAsh(N)" ,"monitoring_device" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t11",targetState="setRP",cond=whenReply("numberRP"))
				}	 
				state("setRP") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("numberRP(X)"), Term.createTerm("numberRP(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 RP_w = payloadArg(0).toInt()  
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t12",targetState="setAsh",cond=whenReply("valueAsh"))
				}	 
				state("setAsh") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("valueAsh(X)"), Term.createTerm("valueAsh(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 Ash_distance = payloadArg(0).toInt()  
						}
						
						         
						         if(RP_w > 0 && Ash_distance > Ash_limit ){
						CommUtils.outmagenta(" WIS: invio messaggio start")
						CommUtils.outmagenta(" WIS: controllo: condizioni corrette e start")
						forward("startRobot", "startRobot(start)" ,"oprobot" ) 
						
						      	rob = "working"
						      	}
						   
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="idle", cond=doswitchGuarded({ rob === "working"  
					}) )
					transition( edgeName="goto",targetState="polling", cond=doswitchGuarded({! ( rob === "working"  
					) }) )
				}	 
				state("polling") { //this:State
					action { //it:State
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
				 	 		stateTimer = TimerActor("timer_polling", 
				 	 					  scope, context!!, "local_tout_"+name+"_polling", 3000.toLong() )  //OCT2023
					}	 	 
					 transition(edgeName="t13",targetState="manageState",cond=whenTimeout("local_tout_"+name+"_polling"))   
				}	 
				state("idle") { //this:State
					action { //it:State
						CommUtils.outgreen("WIS: waiting for updates...")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t24",targetState="managePosition",cond=whenDispatch("info"))
					transition(edgeName="t25",targetState="manageBurnEnd",cond=whenEvent("burnEnd"))
				}	 
				state("managePosition") { //this:State
					action { //it:State
						CommUtils.outgreen("WIS: changing the position...")
						if( checkMsgContent( Term.createTerm("info(X,Y)"), Term.createTerm("info(X,Y)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 val Y = payloadArg(1)   
								CommUtils.outmagenta("WIS: robPosition -> $Y")
								 RobPos = "$Y"
												if(Y.equals("BURNIN")){	
								CommUtils.outred("WIS: incinerator BURN")
								forward("startBurn", "startBurn(start)" ,"incinerator" ) 
								forward("turnLedOn", "turnLedOn(X)" ,"monitoring_device" ) 
								
												}
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="testAdd", cond=doswitchGuarded({ RobPos.equals("ASHOUT")  
					}) )
					transition( edgeName="goto",targetState="idle", cond=doswitchGuarded({! ( RobPos.equals("ASHOUT")  
					) }) )
				}	 
				state("manageBurnEnd") { //this:State
					action { //it:State
						CommUtils.outred("WIS: incinerator STOP BURN")
						forward("turnLedOff", "turnLedOff(X)" ,"monitoring_device" ) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="idle", cond=doswitch() )
				}	 
				state("testAdd") { //this:State
					action { //it:State
						forward("addAsh", "addAsh(X)" ,"monitoring_device" ) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="wait_message", cond=doswitch() )
				}	 
			}
		}
} 
