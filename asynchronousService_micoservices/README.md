# Asynchronous sevice

Per aquest projecte em continuat amb el projecte dels microserveis. Ara es pot eliminar un usuari i les seves notes sense que users i notes parlin directament. 

## Canvis

Els canvis s'han produït en: 

1. Users

    * A la classe restUserController s'ha afegit un deleteMapping per eliminar els usuaris
    
    * S'ha creat la classe CustomOutputEventSourcing que és la encarregada de enviar missatges quan s'elimina un usuari
    
    * S'ha afegit un nou mètode al UseCases per cridar al repositori que elimini al usuari i enviar el id del usuari al CustomOutputEventSourcing
    
2. Notes

    * S'ha creat la classe LogSink encarregada de consumir els missatges enviats per users 
    
    * S'ha afegit un metode per cridar al repositori i que aquest elimini les notes d'un usuari al UseCases 

També, tant a notes com users, s'ha indicat la queue a la que envien/reben missatges. 
La cua és aish i el grup de consumidors de notes és consumers-aish

## Funcionament

Quan un usuari entre per url localhost:8080/users/api/deleteUser/{user} es crida, mitjançant el reverseproxy, el mètode deleteUser del restController de Users. Aquest el que fa és avisar a useCases que s'ha d'eliminar un usuari al mètode deleteUser(String nick).

Un cop el té el useCases, aquest fa dos crides: 

1. Al delete(String nick) del UserRepository, que elimina al usuari de la seva BDD

2. Al userToDelete(String nick) del CustomOutputEventSourcing

Aquest segon mètode crea el missatge del estil {acció}-{user}. En aquest cas és delete-{user} i l'envia a la queue aish

Per l'altre canto, a notes, la classe LogSink està escoltant i quan rep un missatge separà el String enviat per Users gràcies al " - ", d'aquesta forma amb un simple if else pot determinar que fer ha de fer i a quin usuari. 

Un cop té l'acció i usuari, crida a deleteNotesFromUser(String nick) del notesUseCases i aquest li indica al repositori les notes de quin usuari ha d'eliminar.

## Observacions  

Hem decidit crear i descomposar un missatge i no fer en format json, ja que en termes d'eficiència és molt més ràpida aquesta forma i també per facilitat de lectura del codi, ja que per detectar si l'acció és delete s'utilitza un final String, que és molt més fàcil d'entendre que no pas buscar a quin objecte del domini fa referència i quin és el que crea. 
