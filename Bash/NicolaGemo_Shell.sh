#!/bin/bash

#primo elaborato di sistemi operativi anno 2015/2016 Gemo Nicola
#VR386790

function start {
    #funzione invocata all'avvio rappresenta il menu 
    
    #verifico che il file esista
    if ! [ -e aule.txt ]; then
        touch aule.txt;
        #se non esiste creo il file
    fi
    
    #stampo menu
    clear
    echo MENU:
    echo 1. Prenota
    echo 2. Elimina prenotazione
    echo 3. Mostra aula
    echo 4. Prenotazioni per aula
    echo 5. Esci
    echo Inserisci il numero corrispondente alla tua scelta:
    
    #leggo la scelta e avvio la funzione corrispondente
    read scelta 
    case $scelta in
        1) prenota;;
        2) elimina;;
        3) mostra;;
        4) aula;;
        5) esci;;
        *) start;;
    esac
}

function prenota {
    #funzione che richiede le informazioni per la prenotazione
    #ed esegue controlli sugli input
    clear
    echo Prenotazione:
    echo
    
    #acquisisco aula
    #inizializzo la variabile test che usero nella condizione del while
    test="1"
    while [ "$test" = "1" ]
    do
        echo Inserisci il nome dell"'"aula
        read aula
        #controlla che la variabile aula non sia vuota
        if  [ -z "$aula" ]; then
            echo inserisci un nome valido
            test="1";
            #input non valido rientro nel while
        else 
            test="0";
            #input valido posso continuare
        fi
     done
     
    #acquisisco data
    #inizializzo la variabile test che usero nel while
    test="1"
    while [ "$test" = "1" ]
    do
        echo "Inserisci la data nel formato gg mm aaaa (esempio 06 11 2016)"
        read g m a
        #utilizzo la funzione datacheck per controllare la data
        dataCheck $g $m $a
        #assegno il valore ritornato dalla funzione a test
        test="$?"
        
    done

    #acquisisco l'ora
    #inizializzo la variabile test che usero nel while
    test="1"
    while [ "$test" = "1" ]
    do
        echo Inserisci l"'"orario 8-17
        read ora
        #assegno alla varibile lun la lunghezza della stringa contenuta in "ora"
        lun=${#ora}
        #controllo che l'ora sia formata da 2 caratteri e che sia compresa tra 8 e 17,
        #in caso di errore scarto l'output dell'errore 
        if [ $lun -lt 3 ] && [ $ora -lt 18 ] 2>/dev/null && [ $ora -gt 7 ] 2>/dev/null; then
            #uso il comando sed sulla variabile ora sostituendo il carattere 0
            ora=$(sed 's/0*//' <<<"$ora")
            test="0";
            #input corretto esco dal while
        else
            echo Ora errata, valori ammessi da 8 a 17
            test="1";
            #input errato rientro nel while
        fi
    done
    
    #acquisisco utente
    #inizializzo la variabile test che usero nel while
    test="1"
    while [ "$test" = "1" ]
    do
        echo Inserisci il tuo nome utente
        read utente
        #controllo che la variabile utente non sia vuota
        if  [ -z "$utente" ]; then
            echo inserisci un utente valido
            test="1";
            #input non valido rientro nel while
        else 
            test="0";
            #input valido posso uscire dal while
        fi
     done
     
    #tramite il cat e il grep certo una prenotazione con gli stessi valori
    if [ -z $(cat aule.txt | grep "^$aula;$a$m$g;$ora") ]; then
        echo "$aula;$a$m$g;$ora;$utente">>aule.txt
        echo Prenotazione inserita.
        #se non esiste procedo con la prenotazione
    else
        echo Prenotazione fallita, aula occupata.
        #se la prenotazione esiste gia ritorna errore
    fi
    
    echo
    
    read -p  "Premi INVIO per tornare al menu"
    
    #richiamo il menu
    start
}

function elimina {
    #funzione che elimina una prenotazione
        
    clear
    echo Eliminazione
    echo
    
    #acquisisco aula
    #inizializzo la variabile test che usero nel while
    test="1"
    while [ "$test" = "1" ]
    do
        echo Inserisci il nome dell"'"aula
        read aula
        #controlla che la variabile aula non sia vuota
        if  [ -z "$aula" ]; then
            echo inserisci un nome valido
            test="1";
            #input non valido rientro nel while
        else 
            test="0";
            #input valido posso continuare
        fi
     done
     
    #acquisisco data
    #inizializzo la variabile test che usero nel while
    test="1"
    while [ "$test" = "1" ]
    do
        echo "Inserisci la data nel formato gg mm aaaa (esempio 06 11 2016)"
        read g m a
        #utilizzo la funzione datacheck per controllare la data
        dataCheck $g $m $a
        #assegno il valore ritornato dalla funzione a test
        test="$?"
    done

    #acquisisco ora
    #inizializzo la variabile test che usero nel while
    test="1"
    while [ "$test" = "1" ]
    do
        echo Inserisci l"'"orario 8-17
        read ora
        #assegno alla varibile lun la lunghezza della stringa contenuta in "ora"
        lun=${#ora}
        #controllo che l'ora sia formata da 2 caratteri e che sia compresa tra 8 e 17,
        #in caso di errore scarto l'output dell'errore 
        if [ $lun -lt 3 ] && [ $ora -lt 18 ] 2>/dev/null && [ $ora -gt 7 ] 2>/dev/null; then
            #uso il comando sed sulla variabile ora sostituendo il carattere 0
            ora=$(sed 's/0*//' <<<"$ora")
            test="0";
            #input corretto esco dal while
        else
            echo Ora errata, valori ammessi da 8 a 17
            test="1";
            #input errato rientro nel while
        fi
    done
    
    #certo la prenotazione tramite grep e tramite cut utilizzo il primo
    #campo delimitato da : ovvero il numero della linea da eliminare
    linea=$(grep -n "^$aula;$a$m$g;$ora" aule.txt | cut -f1 -d ":")
    if [ -z $linea ]; then
        echo
        echo Prenotazione non trovata
        #se la variabile linea è vuota ritorno errore
    else
        $(sed -i "$linea d" aule.txt);
        echo
        echo Prenotazione eliminata
        #se la variabile linea esiste procedo con l'eliminazione utilizzando
        #il comando sed configurato per modificare il file tramite -i
    fi
    
    echo
    read -p "Premi INVIO per tornare nel menu"
    start
}

function mostra {
    #funziona che stampa tutte le prenotazioni per un a certa aula
    clear
    echo Prenotazioni per aula:
    echo
    #acquisisco aula
    #inizializzo la variabile test che usero nella condizione del while
    test="1"
    while [ "$test" = "1" ]
    do
        echo Inserisci il nome dell"'"aula
        read aula
        #controlla che la variabile aula non sia vuota
        if  [ -z "$aula" ]; then
            echo inserisci un nome valido
            test="1";
            #input non valido rientro nel while
        else 
            test="0";
            #input valido posso continuare
        fi
    done
    
    clear
    echo Prenotazioni per l"'"aula $aula 
    echo
    echo DATA-------ORA---UTENTE
    #stampo le aule ordinate per data e per ora scartando il primo campo(nome aula)
    #uso il comando sed per sostituire il contarettere ; con degli spazi
    grep "^$aula;" aule.txt | cut -d ";" -f2,3,4 |sort -n -t";" -k1 -k2 | sed "s/;/     /g"
    echo
    read -p "Premi INVIO per continuare"
    start
    
}

function aula {
    #funzione che mostra il numero di prenotazioni per aula
    clear
    
    echo Numero prenotazioni per aula:
    #creo la variabile str contenente i primo campi ordinati e non doppi
    str=$(cut -d ";" -f1 aule.txt | sort | uniq)
    #controllo l'esisteza di campi
    #creo la variabile final che usero come risultato intermedio
    final=""
    if ((${#str} >0)); then
        #il while scorre le righe presenti nella variabile str
        while read p;
        do
        #viene contato e e aggiunto a final il numero di occorrenze della stringa p(nome aula)
        #viene usato ^ per evitare falsi positivi
        final+="$p:$(grep -c "^$p;" aule.txt)
"
        done <<<"$str"
    fi
    #stampa final ordinandolo per il numero di occorrenze
    echo "$final" | sort -t":" -k2
    echo
    read -p "premi INVIO per tornare al Menu."
    
    start
}
function dataCheck {
    #funzione che ritorna 0 se la data è corretta altrimenti 1

    #controllo che il numero di argomenti sia pari a 3
    if (($#!=3)); then
        echo Data errata!
        return "1";
    fi
    #controllo che giorno e mese siano composti da 2 numeri
    #salvo la lunghezza di $1(giorni) in lun
    lun=$1
    lun=${#lun}
    #controllo che la lunghezza del parametro giorni sia 2 
    if ! [ $lun -eq 2 ]; then
        echo Data errata
        return "1";
    fi
    #salvo la lunghezza di $2(mesi) in lun
    lun=$2
    lun=${#lun}
    #controllo che la lunghezza del parametro mesi sia 2
    if ! [ $lun -eq 2 ]; then
        echo Data errata
        return "1";
    fi
        
    #Se l'operazione di confronto crea un errore(il messaggio di errore viene scartato)
    #significa che uno dei parametri non e un intero e quindi la funzione datacheck ritornera "1"
    if [ "$1" -eq "$1" ] 2>/dev/null && [ "$2" -eq "$2" ] 2>/dev/null && [ "$3" -eq "$3" ] 2>/dev/null; 
    then
            :
    else    
        echo Data errata!
        return "1";
    fi
    
    #Se il parametro rappresentante i mesi non e compreso tra 1 e 12 vieni ritornato l'errore
    if [ $2 -lt 1 ] || [ $2 -gt 12 ]; then
        echo Data errata!
        return "1";
    fi
    
    #controllo che il numero dei giorni sia compatibile con i mesi
    case $2 in 
        01|03|05|07|08|10|12) 
            if [ $1 -lt 1 ] || [ $1 -gt 31 ]; then
                echo Data errata!
                return "1";
            fi;;
         02) 
            if ((($3%4==0) && ($3%100!=0))) || (($3%400==0)); then
                #bisestile, intervallo da 1 a 29
                if [ $1 -lt 1 ] || [ $1 -gt 29 ]; then
                 echo Data errata!
                 return "1";
                fi
            else
                #non bisestile intervallo valido da 1 a 28
                if [ $1 -lt 1 ] || [ $1 -gt 28 ]; then
                    echo Data errata!
                    return "1";
                fi
            fi;;
         11|04|06|09) 
            if [ $1 -lt 1 ] || [ $1 -gt 30 ]; then
                echo Data errata!
                return "1";
            fi;;
        *)
            echo Data errata!
            return "1";;
    esac
    
    #salvo l'anno il mese e il giorno attuali nelle seguenti variabili
    tmpY=$(date +%Y)
    tmpM=$(date +%m)
    tmpG=$(date +%d)
    
    #controllo che l'anno di prenotazione non sia oltre l'anno successivo a quello attuale
    if((10#$3>tmpY+1));then 
        echo Non sono consentite prenotazioni oltre l"'"anno prossimo!
        return 1;
    fi
    
    #controllo che la data non sia vecchia
    if((10#$3<tmpY));then 
        echo Data gia passata! 
        return "1";
    elif((10#$3==tmpY));then
        if((10#$2<tmpM));then
            echo Data gia passata! 
            return "1";
        elif((10#$2==tmpM));then
            if((10#$1<=tmpG));then
                echo Data gia passata! Prenotazioni per il giorno stesso non possibili! 
                return "1";
            fi
        fi
    fi
    
    #nessun errore ritorno data valida
    return "0"
}

function esci {
    clear
    echo "*******************"
    echo "*   ARRIVEDERCI   *"
    echo "*******************"
    exit
}

clear
echo "*******************"
echo "*   BENVENUTO!!   *"
echo "*******************"
echo
echo Tramite queste script potrai gestire 
echo le prenotazioni delle aule.
echo 
echo N.B: tieni a mente che le prenotazioni durano un ora e
echo che non è possibile modificare prenotazioni il giorno stesso.
echo
read -p "Per iniziare premi INVIO"

#invoco la funzione che rappresenta il menu
start

