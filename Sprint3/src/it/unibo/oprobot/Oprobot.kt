/* Generated by AN DISI Unibo */ 
package it.unibo.oprobot

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

class Oprobot ( name: String, scope: CoroutineScope, isconfined: Boolean=false  ) : ActorBasicFsm( name, scope, confined=isconfined ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		//val interruptedStateTransitions = mutableListOf<Transition>()
		
				var Position = ""
				var State = ""
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						CommUtils.outyellow("opRobot ready...")
						delay(300) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="invio", cond=doswitch() )
				}	 
				state("invio") { //this:State
					action { //it:State
						request("engage", "engage($MyName,330)" ,"basicrobot" )  
						CommUtils.outblue("$name in ${currentState.stateName} | $currentMsg | ${Thread.currentThread().getName()} n=${Thread.activeCount()}")
						 	   
						CommUtils.outblack("opRobot manda request")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t09",targetState="home",cond=whenReply("engagedone"))
					transition(edgeName="t010",targetState="error",cond=whenReply("engagerefused"))
				}	 
				state("home") { //this:State
					action { //it:State
						CommUtils.outblue("$name in ${currentState.stateName} | $currentMsg | ${Thread.currentThread().getName()} n=${Thread.activeCount()}")
						 	   
						CommUtils.outred("opRobot in HOME...")
						 Position = "HOME"  
						delay(5000) 
						 State = "idle"  
						forward("statoOp", "statoOp(idle_home)" ,"observedactor" ) 
						updateResourceRep( "info($State, $Position)"  
						)
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t111",targetState="handleRP",cond=whenDispatch("startRobot"))
				}	 
				state("handleRP") { //this:State
					action { //it:State
						CommUtils.outblue("opRobot to RP...")
						 State = "work"  
						updateResourceRep( "info($State, $Position)"  
						)
						request("moverobot", "moverobot(0,4)" ,"basicrobot" )  
						forward("statoOp", "statoOp(moving_WasteIn)" ,"observedactor" ) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t212",targetState="handleBurnIn",cond=whenReply("moverobotdone"))
					transition(edgeName="t213",targetState="error",cond=whenReply("moverobotfailed"))
				}	 
				state("handleBurnIn") { //this:State
					action { //it:State
						CommUtils.outyellow("opRobot in wateIn going to burnIn...")
						 Position = "WASTEIN"  
						delay(2000) 
						forward("statoOp", "statoOp(taking_RP)" ,"observedactor" ) 
						forward("pickRP", "pickRP(N)" ,"scale" ) 
						updateResourceRep( "info($State, $Position)"  
						)
						request("moverobot", "moverobot(3,1)" ,"basicrobot" )  
						forward("statoOp", "statoOp(moving_BurnIn)" ,"observedactor" ) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t314",targetState="handleHome2",cond=whenReply("moverobotdone"))
					transition(edgeName="t315",targetState="error",cond=whenReply("moverobotfailed"))
				}	 
				state("handleHome2") { //this:State
					action { //it:State
						CommUtils.outyellow("opRobot in burIn going to HOME...")
						 Position = "BURNIN"  
						delay(2000) 
						updateResourceRep( "info($State, $Position)"  
						)
						request("moverobot", "moverobot(0,0)" ,"basicrobot" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t416",targetState="home2",cond=whenReply("moverobotdone"))
					transition(edgeName="t417",targetState="error",cond=whenReply("moverobotfailed"))
				}	 
				state("home2") { //this:State
					action { //it:State
						CommUtils.outyellow("opRobot HOME waiting for the incinerator to stop...")
						 Position = "HOME"  
						forward("statoOp", "statoOp(waiting_home_end_of_burn)" ,"observedactor" ) 
						updateResourceRep( "info($State, $Position)"  
						)
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t518",targetState="handleBurnOut",cond=whenEvent("burnEnd"))
				}	 
				state("handleBurnOut") { //this:State
					action { //it:State
						CommUtils.outyellow("opRobot in HOME going to gathering ashes...")
						forward("statoOp", "statoOp(moving_burnout)" ,"observedactor" ) 
						request("moverobot", "moverobot(5,3)" ,"basicrobot" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t619",targetState="handleBurnOutGo",cond=whenReply("moverobotdone"))
					transition(edgeName="t620",targetState="error",cond=whenReply("moverobotfailed"))
				}	 
				state("handleBurnOutGo") { //this:State
					action { //it:State
						CommUtils.outyellow("opRobot in BurnOUT going to AshOUT...")
						 Position = "BURNOUT"  
						forward("statoOp", "statoOp(taking_ash)" ,"observedactor" ) 
						delay(2000) 
						updateResourceRep( "info($State, $Position)"  
						)
						request("moverobot", "moverobot(6,4)" ,"basicrobot" )  
						forward("statoOp", "statoOp(moving_ashout)" ,"observedactor" ) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t721",targetState="handleAshOut",cond=whenReply("moverobotdone"))
					transition(edgeName="t722",targetState="error",cond=whenReply("moverobotfailed"))
				}	 
				state("handleAshOut") { //this:State
					action { //it:State
						CommUtils.outyellow("opRobot in AshOUT going to HOME...")
						CommUtils.outred("RESTARTING THE ROUTINE...")
						 Position = "ASHOUT"  
						forward("deposit_ash", "deposit_ash(X)" ,"sonar_device" ) 
						forward("statoOp", "statoOp(deposit_ash)" ,"observedactor" ) 
						delay(2000) 
						updateResourceRep( "info($State, $Position)"  
						)
						request("moverobot", "moverobot(0,0)" ,"basicrobot" )  
						forward("statoOp", "statoOp(end_return_home)" ,"observedactor" ) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t723",targetState="home",cond=whenReply("moverobotdone"))
					transition(edgeName="t724",targetState="error",cond=whenReply("moverobotfailed"))
				}	 
				state("error") { //this:State
					action { //it:State
						CommUtils.outred("ERROR...")
						System.exit(-1) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
				}	 
			}
		}
} 
