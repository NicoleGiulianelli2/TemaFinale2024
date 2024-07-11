### conda install diagrams
from diagrams import Cluster, Diagram, Edge
from diagrams.custom import Custom
import os
os.environ['PATH'] += os.pathsep + 'C:/Program Files/Graphviz/bin/'

graphattr = {     #https://www.graphviz.org/doc/info/attrs.html
    'fontsize': '22',
}

nodeattr = {   
    'fontsize': '22',
    'bgcolor': 'lightyellow'
}

eventedgeattr = {
    'color': 'red',
    'style': 'dotted'
}
evattr = {
    'color': 'darkgreen',
    'style': 'dotted'
}
with Diagram('progetto24Arch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
### see https://renenyffenegger.ch/notes/tools/Graphviz/attributes/label/HTML-like/index
     with Cluster('ctx_waste_incinerator', graph_attr=nodeattr):
          waste_incinerator=Custom('waste_incinerator','./qakicons/symActorSmall.png')
          oprobot=Custom('oprobot','./qakicons/symActorSmall.png')
          wastestorage=Custom('wastestorage','./qakicons/symActorSmall.png')
          incinerator=Custom('incinerator','./qakicons/symActorSmall.png')
     with Cluster('ctx_monitor', graph_attr=nodeattr):
          incineratorservicestatusgui=Custom('incineratorservicestatusgui','./qakicons/symActorSmall.png')
     with Cluster('ctx_monitoringdevice', graph_attr=nodeattr):
          monitoringdevice=Custom('monitoringdevice','./qakicons/symActorSmall.png')
          warningdevice=Custom('warningdevice','./qakicons/symActorSmall.png')
     with Cluster('ctx_basicrobot', graph_attr=nodeattr):
          basicrobot=Custom('basicrobot(ext)','./qakicons/externalQActor.png')
     with Cluster('ctx_basicrobot', graph_attr=nodeattr):
          basicrobot=Custom('basicrobot(ext)','./qakicons/externalQActor.png')
     sys >> Edge( label='signal', **evattr, decorate='true', fontcolor='darkgreen') >> waste_incinerator
     incinerator >> Edge( label='signal', **eventedgeattr, decorate='true', fontcolor='red') >> sys
     sys >> Edge( label='signal', **evattr, decorate='true', fontcolor='darkgreen') >> warningdevice
     waste_incinerator >> Edge(color='magenta', style='solid', decorate='true', label='<goPickRP &nbsp; goBurnin &nbsp; goBurnout &nbsp; goDepositAsh &nbsp; goHomeAsh &nbsp; >',  fontcolor='magenta') >> oprobot
     waste_incinerator >> Edge(color='magenta', style='solid', decorate='true', label='<full<font color="darkgreen"> fullYes fullNo</font> &nbsp; >',  fontcolor='magenta') >> monitoringdevice
     oprobot >> Edge(color='blue', style='solid',  decorate='true', label='<updategui &nbsp; >',  fontcolor='blue') >> incineratorservicestatusgui
     waste_incinerator >> Edge(color='blue', style='solid',  decorate='true', label='<goHome &nbsp; >',  fontcolor='blue') >> oprobot
     incinerator >> Edge(color='blue', style='solid',  decorate='true', label='<ledOn &nbsp; >',  fontcolor='blue') >> warningdevice
     monitoringdevice >> Edge(color='blue', style='solid',  decorate='true', label='<updategui &nbsp; >',  fontcolor='blue') >> incineratorservicestatusgui
     incinerator >> Edge(color='blue', style='solid',  decorate='true', label='<updategui &nbsp; >',  fontcolor='blue') >> incineratorservicestatusgui
     wastestorage >> Edge(color='blue', style='solid',  decorate='true', label='<infoRP &nbsp; >',  fontcolor='blue') >> waste_incinerator
     waste_incinerator >> Edge(color='blue', style='solid',  decorate='true', label='<burn &nbsp; >',  fontcolor='blue') >> incinerator
     wastestorage >> Edge(color='blue', style='solid',  decorate='true', label='<updategui &nbsp; >',  fontcolor='blue') >> incineratorservicestatusgui
     monitoringdevice >> Edge(color='blue', style='solid',  decorate='true', label='<removed &nbsp; >',  fontcolor='blue') >> waste_incinerator
     monitoringdevice >> Edge(color='blue', style='solid',  decorate='true', label='<ledBlink &nbsp; >',  fontcolor='blue') >> warningdevice
diag
