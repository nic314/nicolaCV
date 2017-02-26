.section .data #legge un numero da console e lo ritorna su eax  
stringa:
    .ascii ""

.section .text
.global atoi
.type atoi, @function



atoi:
    movl $0,%eax        #azzero eax che uno come sommatore  
    pushl %eax



continua:
    movl $3,%eax        #leggo da tastiera
    movl $0,%ebx
    leal stringa,%ecx
    movl $1,%edx
    int $0x80


    movl stringa,%ecx       #sposto il numero su ecx
    popl %eax               #ripristino eax
    cmp $10,%ecx            #guardo se il carattere+/n
    je fine                 #se si ho finito la lettura
    subl $48,%ecx           #trovo il valore dal codice ascii
    movl $10,%ebx           #preparo i registri per la moltiplicazione
    mull %ebx               #eseguo la moltiplicazione
    addl %ecx,%eax          #sommo il nuovo numero al totale
    pushl %eax              #salvo %eax
    
    jmp continua

fine:
    
    ret
    
    
    
    
        

