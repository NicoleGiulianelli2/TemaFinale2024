%====================================================================================
% progetto24 description   
%====================================================================================
dispatch( start, start("") ).
dispatch( burn, burnrequest("") ).
event( signal, signal("") ).
dispatch( ledOn, ledOn("") ).
dispatch( infoRP, infoRP(N) ).
dispatch( doread, doread(N) ).
request( full, fullrequest("") ).
reply( fullYes, fullYes("") ).  %%for full
reply( fullNo, fullNo("") ).  %%for full
dispatch( removed, removed("") ).
request( goPickRP, goPickRP("") ).
request( goBurnin, goBurnin("") ).
dispatch( goHome, goHome("") ).
request( goHomeAsh, goHomeAsh("") ).
request( goBurnout, goBurnout("") ).
request( goDepositAsh, goDepositAsh("") ).
request( doplan, doplan(PATH,STEPTIME) ).
reply( doplandone, doplandone(ARG) ).  %%for doplan
reply( doplanfailed, doplanfailed(ARG) ).  %%for doplan
dispatch( ledBlink, ledBlink("") ).
dispatch( updategui, updategui(RPstate,ashState,InceneratorState,OpPosition) ).
%====================================================================================
context(ctx_waste_incinerator, "localhost",  "TCP", "11800").
context(ctx_monitor, "localhost",  "TCP", "11801").
context(ctx_monitoringdevice, "localhost",  "TCP", "11802").
context(ctx_basicrobot, "localhost",  "TCP", "11810").
context(ctx_basicrobot, "localhost",  "TCP", "11810").
 qactor( basicrobot, ctx_basicrobot, "external").
  qactor( waste_incinerator, ctx_waste_incinerator, "it.unibo.waste_incinerator.Waste_incinerator").
 static(waste_incinerator).
  qactor( oprobot, ctx_waste_incinerator, "it.unibo.oprobot.Oprobot").
 static(oprobot).
  qactor( wastestorage, ctx_waste_incinerator, "it.unibo.wastestorage.Wastestorage").
 static(wastestorage).
  qactor( incinerator, ctx_waste_incinerator, "it.unibo.incinerator.Incinerator").
 static(incinerator).
  qactor( monitoringdevice, ctx_monitoringdevice, "it.unibo.monitoringdevice.Monitoringdevice").
 static(monitoringdevice).
  qactor( warningdevice, ctx_monitoringdevice, "it.unibo.warningdevice.Warningdevice").
 static(warningdevice).
  qactor( incineratorservicestatusgui, ctx_monitor, "it.unibo.incineratorservicestatusgui.Incineratorservicestatusgui").
 static(incineratorservicestatusgui).
