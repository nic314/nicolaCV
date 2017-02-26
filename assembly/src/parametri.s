.section .data #controlla i parametri inserite da riga di comando
                #ritorna 0 se non corretti,1 se 9 9 2, 2 se 3 3 2 

x:
    .long 0
y:  
    .long 0
z:
    .long 0

        

.section .text

 .global parametri
 .type parametri, @function

parametri:
    popl %eax       #salvo in eax l'indirizzo per il ritorno
    popl %ecx       #salvo il numero dei parametri 
    cmp $4,%ecx     #numero dei parametri sia corretto?
    jne error       #se non corretto salto

    popl %ecx       #scarico dallo stack il l`inirizzo del nome
    popl %ecx       #metto in ecx l`ind. al primo parametro

primo:
    movl $0,%edx    #azzero il registro
    mov (%ecx),%dl  #carico il valore contenuto nell`ind. di ecx
    subl $48,%edx   #ricavo il valore dall`ascii
    pushl %edx      #salvo il valore ottenuto nello stack
    movl $10,%ebx   #preparo moltiplicatore
    pushl %eax      #salvo per evitare che venga modificato
    movl x,%eax     #preparo il registro per la moltiplicazione
    mull %ebx       
    popl %eax       #ripristino eax
    popl %edx       #ripristino edx
    addl %edx,x     #sommo il nuovo numero al totale
    addl $1,%ecx    #avanzo nella stringa
    movl (%ecx),%edx
    testb %dl,%dl
    jz secondo_init     #fine parametro
    jmp primo           #numero non finito

secondo_init:
    popl %ecx              #carico l`ind. succ. su ecx 
    movl $0,%ebx           #azzero ebx
secondo:
    movl $0,%edx            #azzero il registro
    mov (%ecx),%dl          #carico il valore contenuto nell`ind. di ecx
    subl $48,%edx           #ricavo il valore dall`ascii
    pushl %edx              #salvo il valore ottenuto nello stack
    movl $10,%ebx           #preparo moltiplicatore    
    pushl %eax              #salvo per evitare che venga modificato
    mull %ebx               
    popl %eax               #ripristino eax
    popl %edx               #ripristino edx
    addl %edx,y             #sommo il nuovo numero al totale
    addl $1,%ecx            #avanzo nella stringa
    movl (%ecx),%edx
    testb %dl,%dl
    jz terzo_init           #fine parametro
    jmp secondo             #numero non finito


terzo_init:
    popl %ecx               #carico l`ind. succ. su ecx
    movl $0,%ebx
terzo:
    movl $0,%edx
    mov (%ecx),%dl          #carico il valore contenuto nell`ind. di ecx
    subl $48,%edx           #ricavo il valore dall`ascii
    pushl %edx              #salvo il valore ottenuto nello stack
    movl $10,%ebx           #preparo moltiplicatore
    pushl %eax              #salvo per evitare che venga modificato
    mull %ebx
    popl %eax               #ripristino eax
    popl %edx               #ripristino edx
    addl %edx,z             #sommo il nuovo numero al totale
    addl $1,%ecx            #avanzo nella stringa
    movl (%ecx),%edx
    testb %dl,%dl
    jz fine_args            #fine parametro
    jmp terzo               #ce ancora roba



fine_args:  #i 3 parametri sono stati salvati in x y z ora 
            #controllo che siano esatti
    cmp $3,x            #controllo se x=3 y=3 z=2
    jne emergenza
    cmp $3,y
    jne emergenza
    cmp $2,z
    jne emergenza
    pushl %eax
    movl $2,%eax        #se si ritorna 2
    jmp return
emergenza:              #controllo se x=9 y=9 z=2
    cmp $9,x
    jne error
    cmp $9,y
    jne error
    cmp $2,z
    jne error
    pushl %eax
    movl $1,%eax        #se si ritorna 1
    jmp return
error:
    pushl %eax          #ricarica l-indirizzo per il ritorno nello stack
    movl $0,%eax        #se nessuna delle precedenti ritorna 0
    jmp return

return:
    ret


    







