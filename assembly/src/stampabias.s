.section .data

meno:
    .ascii "-"
meno_l:
    .long . - meno

virgola5:
    .ascii ".5\n"
virgola5_l:
    .long . - virgola5

acapo:
    .ascii "\n"
acapo_l:
    .long . - acapo


.section .text

 .global stampabias
 .type stampabias, @function

stampabias:

    movl %eax,%ebx  #copia il bias in ebx
    cmp $0,%eax     #controlla se valore negativo
    jl negativo_1   
    jmp positivo_1
negativo_1: 
    pushl %eax      #salva i valori di eax e ebx nello stack
    pushl %ebx      
    movl $4,%eax    #stampa "-"
    movl $1,%ebx
    leal meno, %ecx 
    movl meno_l,%edx
    int $0x80
    popl %ebx       #ripristina eax e ebx
    popl %eax
    subl %ebx,%eax  #inverte il segno di eax
    subl %ebx,%eax
positivo_1:
    movl $2,%ebx    #prepara i registri per la divisione
    movl $0, %edx   
    divl %ebx       #divide bias1 per 2
    cmp $0,%edx     #controllo se ce resto
    je noresto


resto:
    call itoa       #stampa il risultato della divizione
    movl $4,%eax    #stampa ".5" dopo
    movl $1,%ebx    
    leal virgola5, %ecx
    movl virgola5_l,%edx
    int $0x80
    ret

noresto:
    call itoa       #stampa il risultato della divisione
    movl $4,%eax    
    movl $1,%ebx
    leal acapo, %ecx    #stampa"/n"
    movl acapo_l,%edx
    int $0x80
    ret
