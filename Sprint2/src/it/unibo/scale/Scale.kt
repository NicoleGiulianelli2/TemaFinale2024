/* Generated by AN DISI Unibo */ 
package it.unibo.scale

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

class Scale ( name: String, scope: CoroutineScope, isconfined: Boolean=false  ) : ActorBasicFsm( name, scope, confined=isconfined ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		//val interruptedStateTransitions = mutableListOf<Transition>()
		
				
				var RP_number = 0
				
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						CommUtils.outblack("scale | ready")
						delay(1000) 
						subscribeToLocalActor("scale_device") 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
				state("work") { //this:State
					action { //it:State
						CommUtils.outblack("$name | waits ... ")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t025",targetState="handlescaledata",cond=whenEvent("scaledata"))
					transition(edgeName="t026",targetState="handlepolling",cond=whenRequest("pollingRP"))
					transition(edgeName="t027",targetState="handlepick",cond=whenDispatch("pickRP"))
				}	 
				state("handlescaledata") { //this:State
					action { //it:State
						CommUtils.outyellow("$name in ${currentState.stateName} | $currentMsg | ${Thread.currentThread().getName()} n=${Thread.activeCount()}")
						 	   
						if( checkMsgContent( Term.createTerm("scaledata(D)"), Term.createTerm("scaledata(D)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								CommUtils.outmagenta("$name | weight=${payloadArg(0)}")
								 RP_number = RP_number + 1   
								CommUtils.outmagenta("$name | the RP number now is $RP_number ")
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
				state("handlepolling") { //this:State
					action { //it:State
						CommUtils.outblack("scale | polling request from WIS")
						answer("pollingRP", "numberRP", "numberRP($RP_number)"   )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
				state("handlepick") { //this:State
					action { //it:State
						CommUtils.outmagenta("$name | pick request from robot, the RP number is $RP_number")
						 RP_number = RP_number - 1  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
			}
		}
} 
