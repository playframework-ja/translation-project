# You can specialize this file for each language.
# For example, for French create a messages.fr file
#

### views/main.html
#------------------

main.header.home=Principal
main.header.learn=Aprende
main.header.download=Descargas
main.header.community=Comunidad
main.header.code=Código
main.header.ecosystem=Ecosistema
main.header.modules=Módulos
main.header.about=Acerca de

# footer.learn
main.footer.learn=Aprende
main.footer.overview=Introducción
main.footer.firstApp=Tu primera aplicación
main.footer.tutorial=Tutorial
main.footer.manual=Manuales

# footer.community
main.footer.community=Comunidad
main.footer.googleGroup=Grupo de discusión
main.footer.planetPlay=Planet &hearts; Play
main.footer.codeSnippets=Código fuente

# footer.contribute
main.footer.contribute=Colabora
main.footer.sourceCode=Código fuente
main.footer.bugTracking=Reporta errores
main.footer.contributors=Colaboradores
main.footer.integrationServer=Integración continua

# footer.download
main.footer.download=Download
main.footer.releases=Versiones
main.footer.nightly=Versiones nocturnas
main.footer.license=Licencia Apache 2

# footer message
main.footer.message.authors=Play! es desarrollado por <a href="http://guillaume.bort.fr/">guillaume bort</a> y <a href="http://www.zenexity.com/">zenexity</a>
main.footer.message.license=Distribuido bajo licencia apache 2
main.footer.message.hosting=Hosteado en <a href="http://www.playapps.net/">playapps.net</a>

### views/Application/index.html
#-------------------------------

# main panel, introduction section
index.introduction.title=\
  Con Play framework desarrollar aplicaciones web con Java y <a href="http://scala.playframework.org/">Scala</a>... ¡vuelve a ser divertido!

index.introduction.content=\
  Por fin, un framework para desarrollar aplicaciones web, hecho por desarrolladores de aplicaciones web. \
  Descubre una alternativa ágil a los stacks empresariales de Java. \
  Play hace hincapié en la productividad de los desarrolladores y está orientado a aplicaciones REST.

# overview panel (features)

index.features.title=¿Por qué Play?

index.features.refresh=¡Edita el código y aprieta F5!
index.features.refresh.content=\
  Puedes editar tus archivos de Java, guardar los cambios, refrescar el explorar y ver los resultados al instante. \
  No necesitas compilar, deployar ni reiniciar el servidor.

index.features.stateless=Sin estado
index.features.stateless.content=\
  Play es un verdadero sistema "Share nothing", listo para desarrollar aplicaciones REST. \
  Escala fácilmente ejecutando múltiples instancias de la misma aplicación en diversos servidores.

index.features.template=Sistema de templates eficiente
index.features.template.content=\
  Un elegante sistema de templates basado en Groovy. Permite heredar de templates, hacer includes y desarrollar tus propios tags.

index.features.async=Asincrónico
index.features.async.content=\
  Basado en el modelo IO Non blocking, te permite crear aplicaciones web modernas basadas en long polling y WebSockets.

index.features.fullStack=Un stack completo
index.features.fullStack.content=\
  Trae todo lo que necesitás para crear impresionantes aplicaciones web. \
  Provee integration con Hibernate, OpenID, Memcached... y un completo sistema de plugins.

index.features.java=¡Es Java!
index.features.java.content=\
  Desarrolla tus apliaciones con Java, utilizando cualquiera de las numersosas librerías disponibles, utilizando tu IDE favorito. \
  Se integra fácilmente con eclipse, netbeans o IntelliJ.

index.features.fixErrors=Arregla los errores rápido
index.features.fixErrors.content=\
  Cuando ocurre un error, play te muestra el código que produjo el error y la línea precisa donde se originó el problema, \
  incluso en los templates.

index.features.fun=Productividad
index.features.fun.content=\
  No más perder el tiempo esperando que tu servidor reinicia, \
  aumentá tu productividad y completá tus projectos más rápido. 

# right panel download section
index.download.title=¡Consíguelo ya!
index.download.latest=Descarga %s, la última versión estable.
index.download.others=O bájate 
index.download.othersLink=otras versiones anteriores del framework

# right panel learn section
index.learn.title=Aprende
index.learn.content=\
  Lee la <a href="/documentation/latest/overview">&{index.learn.overview}</a>, \
  haz una aplicación paso a paso siguiendo el <a href="/documentation/latest/guide1">&{index.learn.tutorial}</a>, \
  y revisa la <a href="/documentation">&{index.learn.documentation}</a>.

index.learn.overview=introducción a Play
index.learn.tutorial=tutorial
index.learn.documentation=documentación

# right panel get help section
index.help.title=Recibe ayuda
index.help.content=\
  Únete a la <a href="http://groups.google.com/group/play-framework" target="_blank">&{index.help.googleGroup}</a>, \
  haz preguntas en <a href="http://stackoverflow.com/tags/playframework" target="_blank">&{index.help.stackOverflow}</a>, \
  comparte tu <a href="/community/snippets">&{index.help.code}</a> con la comunidad, \
  y <a href="/community/events">&{index.help.meet}</a> a otros desarrollares.

index.help.googleGroup=lista de discusión
index.help.stackOverflow=Stackoverflow
index.help.code=código
index.help.meet=conoce

# right panel contribute section
index.contribute.title=Colabora
index.contribute.content=\
  Reporta <a href="http://play.lighthouseapp.com/" target="_blank">&{index.contribute.bugReports}</a>, \
  descárgate el <a href="http://github.com/playframework/play" target="_blank">&{index.contribute.source}</a>, \
  y <a href="http://play.lighthouseapp.com/projects/57987/contributor-guide" target="_blank">&{index.contribute.contributeBack}</a> con el proyecto.

index.contribute.bugReports=errores
index.contribute.source=código fuente desde github
index.contribute.contributeBack=contribuye

# right panel twitter section
index.twitter.title=¿Qué dicen en twitter?

# right panel events section
index.events.title=Próximos eventos
index.events.checkAll=Ver todos los eventos

### views/Documentation/page.html
#--------------------------------

documentation.title=Manuales, tutoriales & referencias
documentation.browse=Consulte
documentation.contentsTable=Contenidos
documentation.contents=Contenidos
documentation.next=Próximo
documentation.versions=Elija la versión

documentation.search=Buscar
documentation.search.help=Busque con google

documentation.books=Libros

documentation.comments=Comentarios
documentation.comments.text=
  Utilice este formulario para enviar correcciones y sugerencias acerca de la documentación de esta página. \ 
  Por favor, envíe las preguntas acerca del framework a \
  la <a href="http://groups.google.com/group/play-latam?hl=es">lista de discusión en español</a> o al \
  la <a href="http://groups.google.com/group/play-framework?hl=es">lista de discusión en inglés</a>. \
  Pedidos de soporte, informe de errores y comentarios fuera de tema serán eliminados sin previo aviso.

### views/Application/download.html
#----------------------------------

download.title=%s descargas, y contando...
download.text=\
  Cada versión de play contiene todas las dependencias, la documentación completa, \
  una batería de aplicaciones de ejemplo y soporte para las IDEs más populares y numerosos editores de texto.

download.latest=Consigue la última versión
download.latest.text=La última versión es  <strong>%s</strong> (%s).

download.upcoming=Próxima versión
download.upcoming.test=No te pierdas la oportunidad de probar la próxima versión de play.

download.older=Versiones anteriores
download.older.text=\
  ¿Estás buscando alguna de las versiones anteriores de play? Puedes encontrar algunas de las últimas versiones aquí.

download.night=Versiones nocturnas
download.night.text=\
  ¿Te sients intrépido? Anímate, y prueba alguna de las versiones nocturnas.


### views/Application/code.html
#------------------------------

code.title=Colabora con Play framework

# right panel
code.links.source=Código fuente
code.links.bugTracking=Bug Tracking
code.links.contributors=Colaboradores
code.links.security=Reportes de seguridad
code.links.integrationServer=Integración contínua
code.links.download=Descarga play! framework

# content page

code.license=Licencia
code.license.text=\
  El código fuente de Play framework es ditribuido en los términos de la \
  <a href="http://www.apache.org/licenses/LICENSE-2.0.html">licencia Apache 2</a>.
  
code.bugs=Reportando errores
code.bugs.text=\
  Usamos <a href="http://play.lighthouseapp.com">Lighthouse</a> para registrar los incidentes. \
  Los reportes de errores son de suma utilidad para nosotros, así que tómate el tiempo para reportar errores y solicitar nuevas prestaciones.
  Siempre estamos a la espera de parches y mejoras para el código de Play.
  De hecho, si los reportes de errores traen parches asociados serán resueltos mucho más rápido.

code.getCode=Consigue el código
code.getCode.text=\
  El código de Play framework se encuentra disponible en Github, utilizando Git como sistema de control de versiones.
  Puedes conseguir el código fuente ejecutando el siguiente comando:
code.getCode.text2=\
  Sin embargo, el historial de cambios del proyecto es bastante pesado. \
  Puedes limitar la cantidad de commits para obtener una copia más liviana del historial utilizando la opción --depth:

code.source=Compilando Play desde el código fuente
code.source.text=\
  Necesitas tener Ant instalado en tu sistema para compilar el código fuente de Play. Simplemente dirígete al directory framework/ y ejecuta:
code.source.text2=\
  ¡Eso es todo! Si deseas ejecutar la batería de pruebas en tu computadora, simplemente escribe:
  
code.contribute=Colaborando con Play
code.contribute.text=\
  La mejor manera de colaborar con Play es encontrando y reportando errores, \
  escribiendo pruebas para detectarlos, y mejorando la documentación. \
  Siempre estamos a la espera de parches y mejoras para el código de Play. \
  De hecho, si los reportes de errores traen parches asociados serán resueltos mucho más rápido.
code.contribute.text2=\
  Si deseas modificar el código, asegúrate de seguir nuestras directivas de diseño.
  Presta especial atención para no romper la compativilidad con los módules y versiones anteriores de Play,
  y procura no aumentar la complejidad del framework si no tienes una razón muy buena para hacerlo.
code.contribute.text3=\
  ¿Tienes un parche para enviarnos? Revisa la <a href="http://play.lighthouseapp.com/projects/57987/contributor-guide">guía del colaborador</a>.

### views/Application/ecosystem.html
#-----------------------------

ecosystem.title=Soporte y consultoría profesional

ecosystem.consulting=Consultoría, capacitación y soporte

ecosystem.zenexity.text=\
  Al ser la compañía que está detrás de Play Framework, \
  <a href="http://www.zenexity.com">Zenexity</a> puede ayudarle a comprender \
  los conceptos clave del framework, para que pueda aprovechar todo su potencial.
ecosystem.zenexity.text2=\
  Podemos auditar sus aplicaciones de play, capacitar a su personal, \
  poner en marcha su proyecto y guiarlo para que pueda integrarlo con sus sistemas actualmente en producción. \
  También ofrecemos soporte profesional para aplicaciones críticas corriendo en Play.

ecosystem.lunatech.title=Lunatech Research, Netherlands
ecosystem.lunatech.text=\
  <a href="http://www.lunatech-research.com/">Lunatech</a> viene contribuyendo con Play desde sus inicios. \
  Podemos ayudarle a diseñar una arquitectura elegante, \
  reutilizando sus servicios y desarrollando servicios web REST y aplicaciones que sean sólidas y mantenibles.
ecosystem.lunatech.text2=\
  Si desea integrar aplicaciones hechas con Play con sus servicios de Java EE actualmente en producción \
  pero no sabe cómo hacerlo, simplemente consúltenos. Brindamos capacitación técnica y \
  también proveemos soporte de tercera línea para Play!, \
  con una acuerdo de servicio (SLA) para consultas de desarrolladores, \
  corrección de errores del framework y desarollo de nuevas prestaciones.

ecosystem.hosting=Hosting

ecosystem.heroku.title=Heroku
ecosystem.heroku.text=\
  <a href="http://www.heroku.com">Heroku</a> (se pronucia je-ro-ku) \
  es una plataforma de cloud computing - una nueva manera de contruir e implementar aplicaciones web.
ecosystem.heroku.text2=\
  Brinda soporte nativo para Play, sin nicesidad de crear ningún módulo, ni empaquetar la aplicación en un war, \
  ni ningún paso extra, simplemente su aplicación de Play lista para ser implementada en la nube de heroku.

ecosystem.playapps.title=playapps.net
ecosystem.playapps.text=\
  <a href="http://www.playapps.net">Playapps.net</a> es un entorno de implementación liviano \
  especialmente diseñado para tener sus aplicaciones de Play corriendo de manera rápida y eficiente.
ecosystem.playapps.text2=\
  Fue creado teniendo en mente la misma simplicidad que caracteriza a Play, \
  y cada slot incluye todos los servicios que necesita para ejecutar aplicaciones \
  sin la complicación de administrar su propio servidor.


### views/Application/modules.html
#---------------------------

modules.title=Módulos contribuidos por la comunidad

modules.repository=Repositorio de módulos
modules.repository.text=\
  Aquí podrás encontrar excelentes módulos contribuidos por la comunidad de play.
  Elige el que más te guste y agrega de manera instantánea nueva funcionalidad y mayor poder a tus aplicaciones.
  ¿Buscas ayuda acerca de los módulos? Revisa el <a href="/documentation/latest/modules">manual</a> de play.

modules.allModules=Todos los módulos

modules.defaultSearch=¿Buscas algo en especial?

modules.details=Consulta los detalles y versiones del módulo


### views/Application/about.html
#-------------------------

about.title=Acerca de este sitio
about.welcome=Bienvenido a playdoces
about.welcome.text=Aquí podrá encontrar la traducción de la documentación de play framework para la comunidad hispano-hablante. 
about.disclaimer=Este no es el sitio oficial de play. 
about.disclaimer.text=Este proyecto NO ESTÁ desarrollado por <a href="http://www.playframework.org/">playframework.org</a>.
about.disclaimer.text2=Por favor, no haga preguntas acerca del contenido de este sitio en playframework.org. 

about.acknowledment=Agradecimientos
about.acknowledment.text=Este sitio se ha basado en %s, que es desarrollado y mantenido por %s.

about.contribute=Colabora con la traducción de Play!
about.contribute.text=\
La documentación de Play es extensa y detallada, y es sin duda uno de los puntos fuertes del framework. \
Lograr una traducción que sea precisa y amena al mismo tiempo requiere mucho tiempo y esfuerzo, \
es por eso que apelamos a la ayuda de la comunidad para llevar adelante esta tarea. \
Si estás interesado en colaborar lee nuestra <a href="%s">guía para colaborar con la traducción de Play!</a>.  

about.translators=Traductores
about.translators.text=Este sitio se encuentra en <a href="https://github.com/opensas/playdoces"/>github</a> y es mantenido por los siguientes traductores.
