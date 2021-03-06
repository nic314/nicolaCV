.section .data  #chiede di reinserire il codice di configurazione 
                #ritorna 2 se 3 3 2, 1 se 9 9 2, 0 se codice errato

x:              #varibile utilizzata per la prima cifra
    .long 0
y:              #varibile utilizzata per la seconda cifra
    .long 0
z:              #varibile utilizzata per la terza cifra
    .long 0

inputerror:     #stringa da stampare in caso di errore
    .ascii "Codice errato, inserire nuovamente il codice\n"
inputerror_l:
    .long . - inputerror


tmp:            #valore utilizzato per salvare il
    .long 0     #valore letto da tastiera


.section .text

    .global inputconsole
    .type inputconsole, @function

inputconsole:
    
    movl $4,%eax            #stampo il messaggio d`errore
    movl $1,%ebx
    leal inputerror, %ecx
    movl inputerror_l,%edx
    int $0x80

    movl $0,%ebx            #azzero i registri
    movl $0,%ecx
    movl $0,%edx
    movl $0,%eax
    pushl %eax              #salvo il valore di eax 
                            #che usero come somma parziale

primo:
    movl $3,%eax            #effettuo la lettura di un carattere
    movl $0,%ebx
    leal tmp,%ecx
    movl $1,%edx
    int $0x80

    movl tmp,%ecx           #sposto il valore appena letto
    popl %eax               # in ecx e ripristino eax
    cmp $32,%ecx            #controllo che non ci siano altri valori
    je secondo_init         #se primo numero e letto passo al secondo
    cmp $10,%ecx            #controllo se la stringa e finita
    je fine                 #in caso affermativo ho finito
    subl $48,%ecx           #ricavo il valore reale dal cod.ascii
    movl $10,%ebx           #preparo il moltiplicatore
    mull %ebx               #eseguo la moltiplicazione tra eax e ebx
    addl %ecx,%eax          #aggiungo il nuovo numero ad eax
    pushl %eax              #salvo eax nello stack
    
    jmp primo

secondo_init:
    movl %eax,x             #salto il primo numero su x
    movl $0,%eax            #inizializzo per il secondo numero
    pushl %eax              #salvo il registro eax nello stack
    

secondo:
    movl $3,%eax            #leggo da tastiera il prossimo valore
    movl $0,%ebx
    leal tmp,%ecx
    movl $1,%edx
    int $0x80


    movl tmp,%ecx           #sposto il valore letto in ecx
    popl %eax               #rispristino eax
    cmp $32,%ecx            #controllo che non ci siano altri valori
    je terzo_init           #se primo numero e letto passo al terzo
    cmp $10,%ecx            #controllo se la stringa e finita
    je fine                 #in caso affermativo ho finito
    subl $48,%ecx           #ricavo il valore reale dal cod.ascii
    movl $10,%ebx           #preparo il moltiplicatore
    mull %ebx               #eseguo la moltiplicazione tra eax e ebx
    addl %ecx,%eax          #aggiungo il nuovo numero ad eax
    pushl %eax              #salvo eax nello stack
    
    jmp secondo

terzo_init:
    movl %eax,y             #salto il primo numero su y
    movl $0,%eax            #inizializzo per il secondo numero
    pushl %eax              #salvo il registro eax nello stack
    

terzo:
    movl $3,%eax            #leggo da tastiera il prossimo valore
    movl $0,%ebx
    leal tmp,%ecx
    movl $1,%edx
    int $0x80


    movl tmp,%ecx           #sposto il valore letto in ecx
    popl %eax               #rispristino eax
    cmp $32,%ecx            #controllo che non ci siano altri valori
    je error_scaricobuffer  #se ci sono scarico il buffer e lancio error
    cmp $10,%ecx            #controllo se la stringa e finita
    je fine                 #in caso affermativo ho finito
    subl $48,%ecx           #ricavo il valore reale dal cod.ascii
    movl $10,%ebx           #preparo il moltiplicatore
    mull %ebx               #eseguo la moltiplicazione tra eax e ebx
    addl %ecx,%eax          #aggiungo il nuovo numero ad eax
    pushl %eax              #salvo eax nello stack
    
    jmp terzo


error_scaricobuffer:
    movl $3,%eax            #leggo da tastiera il prossimo valore
    movl $0,%ebx
    leal tmp,%ecx
    movl $1,%edx
    int $0x80

    movl tmp,%ecx           #se /n ho scaricato il buffer
    cmp $10,%ecx 
    je error                #e eichiedo i valori        
    jmp error_scaricobuffer #altrimenti continuo
    


fine: 
    movl %eax,z             #salto il terzo valore in z

    cmp $3,x                #controllo se x=3,y=3,z=2
    jne emergenza
    cmp $3,y
    jne emergenza
    cmp $2,z
    jne emergenza           #in caso affermativo carico 2 in eax
    movl $2,%eax            #e ritorno
    jmp return
emergenza:
    cmp $9,x                #controllo se x=9,y=9,z=2
    jne error               
    cmp $9,y
    jne error
    cmp $2,z
    jne error               #in caso affermativo carico 1 in eax
    movl $1,%eax            #e ritorno
    jmp return
error:
    movl $0,%eax            #se x,y,z diversi da 332 e 992 ritorno 0
    jmp return

return:
    ret
