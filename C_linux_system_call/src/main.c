/** @file
*SUPPOSIZIONI: i valori inseriti nel file sono numeri interi,come i risultati, il formato delle iterazioni è "<id> <num1> <op> <num2>",
*L'elaborato e strutturato in un unico file contente 3 funzioni più la funzione main.
*Il programma analizza il file di testo iterazioni.txt, crea un array di strutture "iter" ogni struttura rappresenta un iterazione,la memoria condivisa è un array di struct "iter" dove ogni processo ha una struttura ad esso associata.
*Per lo svolgimento sono stati usati un numero di semafori pari a NPROC*2+1, dove i primi NPROC semafori inizializzati ad 1 sono usati dal padre per capire se un processo è occupato mentre i seguenti NPROC semafori inizializzati a 0 sono usati dal padre per avviare i processi figlio una volta che sono state caricate le informazioni relative all'iterazione, il semaforo rimanente viene usato come un semaforo intero e tiene traccia del numero di processi liberi.
Il comando di terminazione K viene inviato ai processi tramite la variabile operatore presente nella struct 'iter'.
Il risultato dell'operazione viene stampato sul file risultati.txt
*/
#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <unistd.h>
#include <sys/ipc.h>
#include <sys/shm.h>
#include <sys/types.h>
#include <sys/sem.h>

struct sembuf semb;//utilizzata per configurare semop()
int semaforo;// id del semaforo

/** @name funzioni */
/*@{ */
/** @brief Trova la sottostringa seguente usando il delimitatore dato. 
* @param source La stringa originale.
* @param inizio La posizione da cui iniziare a creare la nuova sottostringa.
* @param d Il carattere usato come delimitatore.
* @param result L'indirizzo in cui sara salvata la nuova sottostringa.
* @return Il numero di caratteri che compongono la nuova sottostringa.
*/
int findNext(char *source,int inizio,char d,char **result){
    int i=0;//variabile usata per contara i caratteri
    while(source[inizio+i]!=d)//conto il numero di caratteri da leggere
        i=i+1;
    char *tmp=malloc(i*sizeof(char));
    int j;
    for(j=0;j<i;j++)//creo la nuova sottostringa
        tmp[j]=source[inizio+j];
    *result=tmp;//salvo la nuova sottostringa

    return i+1;
}
/*@} */
/** @name funzioni */
/*@{ */
/** @brief Funzione usata per simulare il comportamento signal dei semafori, incrementando il valore del semaforo
* @param sem_number Il semaforo su cui effettuare l'operazione
*/
void sem_signal(int sem_number) {
    semb.sem_op=1;
    semb.sem_num=sem_number;               //setto il numero del semaforo

    if (semop(semaforo, &semb, 1) == -1) {  // eseguo la signal 
        write(2,"errore signal\n",14);
        exit(1);
    }
}
/*@} */
/** @name funzioni */
/*@{ */
/** @brief Funzione usata per simulare il comportamento wait dei semafori, decrementando il valore del semaforo
* @param sem_number Il semaforo su cui effettuare l'operazione
*/
void sem_wait(int sem_number) {
    semb.sem_op=-1;
    semb.sem_num = sem_number;                     //setto il numero del semaforo 

    if (semop(semaforo, &semb, 1) == -1) {        // eseguo la wait 
        write(2,"errore wait\n",12);
        exit(1);
    }
}
/*@} */
int main()
{
    char *buffer=malloc(1024*sizeof(char));//utilizzato per leggere il contenuto del file
    int NPROC=0;//numero di processi da creare
    int iterazioni=0;//numero di iterazioni da effettuare
    char s[1024];//utilizzato nelle sprintf() per memorizzare la stringa da stampare
        
    struct iter {
        int nr_iter;//numero dell'iterazione
        int id;//processo da utilizzare
        int a;// primo operando
        int b;//secondo operando
        int op;// operazione
        int res;//risultato
        int resPronto;//-1 se risultato non pronto 
    };
    union semun {   // struct usata per la gestione del controllo sui semafori 
        int val;
        struct semid_ds *buf;
        ushort *array;
    } st_sem;

    semb.sem_flg=0;//inizializzo flag di funzionamento

    int filedesc=open("iterazioni.txt",O_RDONLY);// apro il file da leggere
    if(filedesc<0){
        write(2,"errore nell'apertura del file\n",30);
        exit(1);
    }
    
    int nread=0;//numero di caratteri letti
    int tmpRead=0;
    int i=0;
    do{
        i=i+1;
        buffer=realloc(buffer,i*1024*sizeof(char));//alloco lo spazio
        tmpRead=read(filedesc,buffer+(i-1)*1024,1024);//salvo il numero di caratteri letti
        if(tmpRead<0){
            write(2,"errore nella lettura del file\n",30);
            exit(1);
        }
        nread=nread+tmpRead;
        
    }while(tmpRead>=1024);// se numero caratteri maggiore di 1024 continuo a leggere

        
        
        


    int start=0;
    char *processiS;
    start=start+findNext(buffer,start,'\n',&processiS);//leggo il numero di processi da creare
    NPROC=atoi(processiS);//salvo il numero di processi da creare
    free(processiS);



    //creo e riempio l'array contente i lavori
    struct iter *lavori=malloc(1);

    for(i=0;start<nread;i++){//leggo i dati dal file
        lavori=realloc(lavori,(i+1)*sizeof(struct iter));

        lavori[i].nr_iter=i+1;//salvo il numero dell'iterazione
        char *temp;
        start=start+findNext(buffer,start,' ',&temp);//salvo il processo
        lavori[i].id=atoi(temp);

        start=start+findNext(buffer,start,' ',&temp);//salvo il primo operando
        lavori[i].a=atoi(temp);

        start=start+findNext(buffer,start,' ',&temp);//salvo l'operazione
        lavori[i].op=temp[0];    

        start=start+findNext(buffer,start,'\n',&temp);//salvo il secondo operando
        lavori[i].b=atoi(temp);
            
        lavori[i].resPronto=-1;//

    }
    free(buffer);
    int risultati[iterazioni=i];//array contenente i risultati

    //inizializzazione semafori

    key_t skey=ftok("main.c", 'a');//creo la chiave per i semafori
    if ((semaforo = semget(skey, NPROC*2+1, 0777|IPC_EXCL|IPC_CREAT)) == -1){//
        write(2,"creazioni semafori fallita\n",27);
        exit(1);
    }
    int semPL=NPROC*2;//numero del semaforo processi liberi
    st_sem.val = 1;//inizializzo i primi nproc semafori a 1
    for(i=0;i<NPROC;i++){

        if (semctl(semaforo, i, SETVAL, st_sem)==-1) {
            write(2,"errore inizializzazione semafori\n",33);
            semctl(semaforo, 0, IPC_RMID, 0);
            exit(1);
        }
    }
    st_sem.val = 0;// inizializzo i seguenti nproc semafori a 0
    for(i=NPROC;i<NPROC*2;i++){

        if (semctl(semaforo, i, SETVAL, st_sem)==-1) {
            write(2,"errore inizializzazione semafori\n",33);
            semctl(semaforo, 0, IPC_RMID, 0);
            exit(1);
        }
    }
    st_sem.val = NPROC;//inizializzo semaforo processi liberi
    if (semctl(semaforo, i, SETVAL, st_sem)==-1) {
        write(2,"errore inizializzazione semafori\n",33);
        semctl(semaforo, 0, IPC_RMID, 0);
        exit(1);
    }

    //inizializzo memoria condivisa
    key_t mkey=ftok("main.c", 2);//creo chiave memoria condivisa
    int shmID = -1;//id della memoria condivisa
    if ((shmID = shmget(mkey, NPROC*sizeof(struct iter), IPC_CREAT|0666)) == -1)  {
        write(2,"errore allocazione memoria condivisa\n",38);
        semctl(semaforo, 0, IPC_RMID, 0);
        exit(1);
    }



    //creazione figli
    for(i=0;i<NPROC;i++){
        switch (fork()) {
            case -1:
                write(2,"PADRE:errore creazione figli\n",29);
                shmctl(shmID,IPC_RMID,0);
                semctl(semaforo, 0, IPC_RMID, 0);
                exit(1);
            case 0://fiflio
            {
                    write(1,s,sprintf(s,"   FIGLIO%i: sono stato creato\n",i+1));      //codice figli
                    struct iter *iterC;//punto di attacco
                    //attacca memoria
                    if ((iterC =(struct iter *)shmat(shmID, NULL, 0666)) ==(struct iter *) -1) {
                        write(2,"   FIGLIO:errore attaccatura memoria condivisa\n",48);
                        shmctl(shmID,IPC_RMID,0);//rimuovo memoria condivisa
                        semctl(semaforo, 0, IPC_RMID, 0);//rimuovo semafori
                        exit(1);
                    }

                while(1){
                    
                    sem_wait(NPROC+i);//fiflio in attesa del padre
                   
                    write(1,s,sprintf(s,"   FIGLIO%i: leggo memoria condivisa\n",i+1));
                    
                    if(iterC[i].op==47){// divisione
                        if(iterC[i].b==0){                            
                            write(1,s,sprintf(s,"   FIGLIO%i:divisione per 0 non possibile\n",i+1));
                        }else{
                            iterC[i].res=iterC[i].a/iterC[i].b;
                        }
                    }
                    else if(iterC[i].op==43)//somma
                        iterC[i].res=iterC[i].a+iterC[i].b;
                    else if(iterC[i].op==42)//moltiplicazione
                        iterC[i].res=iterC[i].a*iterC[i].b;
                    else if(iterC[i].op==45)//sottrazione
                        iterC[i].res=iterC[i].a-iterC[i].b;
                    else if(iterC[i].op==75){//operatore K
                        int size=sprintf(s,"   FIGLIO%i:Chiusura processo\n",i+1);
                        write(1,s,size);              
                        sem_signal(i);
                        sem_signal(semPL);
                        shmdt(iterC);
                        exit(1);
                    }else{
                        int size=sprintf(s,"   FIGLIO%i:Operando sconosciuto\n",i+1);
                        write(2,s,size);
                        sem_signal(i);
                        sem_signal(semPL);
                        shmdt(iterC);
                        exit(1);                        
                    }
                    
                    iterC[i].resPronto=1;
                    write(1,s,sprintf(s,"   FIGLIO%i: iterazione %i completata\n",i+1,iterC[i].nr_iter));
                    sem_signal(i);//sblocco padre se in attesa
                    sem_signal(semPL);//aggiorno il semaforo processi liberi
                }
                
            }
            default:; 
        }
    }

    struct iter *iterC;//attacco memoria condivisa
    if ((iterC =(struct iter *)shmat(shmID, NULL, 0666)) == (struct iter *) -1) { 
        write(2,"PADRE: errore attaccatura memoria condivisa\n",45);
	    shmctl(shmID,IPC_RMID,0);
    	semctl(semaforo, 0, IPC_RMID, 0);
        exit(1);
    }
    
    for(i=0;i<NPROC;i++)//inizializzo a -1
        iterC[i].resPronto=-1;

    for(i=0;i<iterazioni;i++){
        if(lavori[i].id !=0){
            //verifica su semaforo se id i e libero
            write(1,s,sprintf(s,"PADRE: Verifico se Processo %i è libero...\n",lavori[i].id));   
            sem_wait(lavori[i].id-1);
            //controllo array sulla meroria condivisa se ce un risultato da salvare
            if(iterC[lavori[i].id-1].resPronto>0)              
                risultati[iterC[lavori[i].id-1].nr_iter-1]=iterC[lavori[i].id-1].res;
            
            //carico in memoria condivisa l'operazione i
            iterC[lavori[i].id-1]=lavori[i];
            //sblocco il processo id
            write(1,s,sprintf(s,"PADRE: Memoria condivisa per il processo %i pronta, avvio il processo\n",lavori[i].id));
            sem_signal(lavori[i].id+NPROC-1);
            sem_wait(semPL);// aggiorno il semaforo processi liberi
        }else{//caso 0
            write(1,s,sprintf(s,"PADRE: Controllo se ci sono processi liberi...\n"));
            sem_wait(semPL);//controllo che ci siano processi liberi
            sem_signal(semPL);
            
            int j;
            for(j=0;j<NPROC;j++){
                if(semctl(semaforo,j,GETVAL,0)>0){//cerco il primo processo libero
                    sem_wait(j);
                    if(iterC[j].resPronto>0)//controllo se ce un risultato da salvare          
                        risultati[iterC[j].nr_iter-1]=iterC[j].res;
            
                    //carico in memoria condivisa l'operazione i
                    iterC[j]=lavori[i];
                    //sblocco il processo id
                    write(1,s,sprintf(s,"PADRE: caso 0,faccio partire il processo %i\n",j+1));
                    sem_signal(j+NPROC);
                    break;
                }
                
            }
            
            
            
            
            

        }
    }
    //aspetto che i processi finiscano e invio l'operando 'K' per segnalare la terminazione
    for(i=0;i<NPROC;i++){
        sem_wait(i);

        if(iterC[i].resPronto>0) //salvo risultato pronto se c'è
            risultati[iterC[i].nr_iter-1]=iterC[i].res;
 
        iterC[i].op='K';//preparo segnale di chiusura
        write(1,s,sprintf(s,"PADRE: invio segnale di terminazione K al processo %i\n",i+1));
        sem_signal(i+NPROC);//sblocco figlio
    }
    //aspetto che tutti i processi figlio si chiudano
    for(i=0;i<NPROC;i++)
        sem_wait(i);
    
    //scrivo su file
    write(1,"Scrittura su file in corso...\n",30);
    int fileres=creat("risulati.txt",O_RDWR|0666);//gestire -1
    if (fileres==-1) {
        write(2,"PADRE: errore scrittura su file\n",32);
        free(lavori);
    	close(filedesc);
    	close(fileres);
    	shmctl(shmID,IPC_RMID,0);
    	semctl(semaforo, 0, IPC_RMID, 0);
        exit(1);
    }
    for(i=0;i<iterazioni;i++){//stampo a video i risultati
        write(fileres,s,sprintf(s,"risultato iterazione %i = %i \n",i+1,risultati[i]));
        write(1,s,sprintf(s,"risultato iterazione %i = %i \n",i+1,risultati[i]));
    }
        
        


    free(lavori);
    close(filedesc);
    close(fileres);
    shmctl(shmID,IPC_RMID,0);
    semctl(semaforo, 0, IPC_RMID, 0);
    return 0;
}
