.section .data #chiede all`utente 3 valori e li ritorna 
                #nei registri %eax %ebx %ecx, in caso di
                #input errato richiede i valori

x:
    .long 0
y:  
    .long 0
z:
    .long 0

tmp:
    .long 0

errore:
    .ascii "Sono stai inseriti valori mancanti o in eccesso, reinserire\n"
errore_l:
    .long . - errore
.section .text

    .global inputpasseggeri
    .type inputpasseggeri, @function

inputpasseggeri:

jmp inizializzazione

error:
    movl $4,%eax
    movl $1,%ebx
    leal errore, %ecx
    movl errore_l,%edx
    int $0x80
    movl $0,x
    movl $0,y
    movl $0,z

inizializzazione:
    movl $0,%ebx            #azzero i registri che usero
    movl $0,%ecx
    movl $0,%edx
    movl $0,%eax
    pushl %eax              #salvo eax nello stack

primo:
    movl $3,%eax            #leggo da tastiera
    movl $0,%ebx
    leal tmp,%ecx
    movl $1,%edx
    int $0x80

    movl tmp,%ecx           #copio il valore letto in ecx   
    popl %eax               #ripristino eax
    cmp $32,%ecx            #se numero finito 
    je secondo_init         #continuo col secondo
    cmp $10,%ecx            #se /n lancio error e richiedo
    je error                
    subl $48,%ecx           #ricavo il numero  dall`ascii
    movl $10,%ebx           #preparo il moltiplicatore
    mull %ebx               #moltiplico eax per eax
    addl %ecx,%eax          #aggiungo il numero al risultato
    pushl %eax              #salvo eax nello stack
    
    jmp primo

secondo_init:
    movl %eax,x             #salvo eax in x
    movl $0,%eax            #inizializzo
    pushl %eax
    
secondo:
    movl $3,%eax            #leggo da tastiera
    movl $0,%ebx
    leal tmp,%ecx
    movl $1,%edx
    int $0x80

    movl tmp,%ecx           #copio il valore letto in ecx   
    popl %eax               #ripristino eax
    cmp $32,%ecx            #se numero finito 
    je terzo_init           #continuo col terzo
    cmp $10,%ecx            #se /n lancio error e richiedo
    je error
    subl $48,%ecx           #ricavo il numero  dall`ascii
    movl $10,%ebx           #preparo il moltiplicatore
    mull %ebx               #moltiplico eax per eax
    addl %ecx,%eax          #aggiungo il numero al risultato
    pushl %eax              #salvo eax nello stack
    
    jmp secondo

terzo_init:
    movl %eax,y             #salvo eax in x
    movl $0,%eax            #inizializzo
    pushl %eax

terzo:
    movl $3,%eax            #leggo da tastiera
    movl $0,%ebx
    leal tmp,%ecx
    movl $1,%edx
    int $0x80

    movl tmp,%ecx           #copio il valore letto in ecx  
    popl %eax               #ripristino eax
    cmp $32,%ecx            #se numero finito
    je error_scaricobuffer  #continuo col terzo
    cmp $10,%ecx            #se /n lancio error e richiedo
    je fine
    subl $48,%ecx           #ricavo il numero  dall`ascii
    movl $10,%ebx           #preparo il moltiplicatore
    mull %ebx               #moltiplico eax per eax
    addl %ecx,%eax          #aggiungo il numero al risultato
    pushl %eax              #salvo eax nello stack
    
    jmp terzo

error_scaricobuffer:
    movl $3,%eax            #scarico il buffer
    movl $0,%ebx
    leal tmp,%ecx
    movl $1,%edx
    int $0x80

    movl tmp,%ecx
    cmp $10,%ecx
    je error                #lancio errore
    jmp error_scaricobuffer
    
fine: 
    movl %eax,z             #salvo eax in z

    movl x,%eax             #ritorno i valori
    movl y,%ebx
    movl z,%ecx
    
return:
    ret
