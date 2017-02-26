.section .data #prende il numero contenuto in %eax e lo stampa senza \n
car:
    .byte 0

.section .text
.global itoa
.type itoa, @function

itoa:

    mov $0, %ecx        #azzero il registro ecx che uso come contatore

continua_a_dividere:
    cmp $10, %eax       #controllo che il numero sia >= 10
    jge dividi          #in caso affermativo salto

    pushl %eax          #salvo nello stack il valore <10 piu significativo 
    inc %ecx            #incremento il contatore
    mov %ecx, %ebx      #copio il contatore nel registro ebx
    jmp stampa          #cifre sono pronte per la stampa nello stack

dividi:
    movl $0, %edx       #azzero il registro edx per la divisione
    movl $10, %ebx      #carico il divisore nel registro ebx
    divl %ebx           #eseguo la divizione tra eax e ebx

    pushl %edx          #salvo il resto nello stack
    inc %ecx            #incremento il contatore
    jmp continua_a_dividere 

stampa:
    cmp $0, %ebx        #se il contatore e 0 sono stati 
    je fine_itoa        #sono stati stampati tutti i numeri
    popl %eax           #altrimenti di carica il numero da stampare
    movb %al, car       #dallo stack ci si aggiunge 48 per avere il
    addb $48, car       #corrispondente simbolo ascii
    dec %ebx            #si diminuisce il contatore
    pushw %bx           #si salta il contatore nello stack
    movl $4, %eax       #per non perderlo e si stampa il valore
    movl $1, %ebx       
    leal car, %ecx
    mov $1, %edx
    int $0x80
    popw %bx            #si rispristina il contatore in ebx
    jmp stampa          
fine_itoa:
    ret














