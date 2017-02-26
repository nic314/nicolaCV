.section .data #punto d`entrata del programma

fail:
    .ascii "Failure controllo codice.Modalità safe inserita\n"

fail_l:
    .long . - fail

din:
    .ascii"Modalità controllo dinamico inserita\n"

din_l:
    .long . - din

em:
    .ascii "Modalità controllo emergenza inserita\n"
em_l:
    .long . - em


.section .text

    .global _start

_start:

    call mode       #ritorna 2,1 o 0 a seconda dell-input

    cmp $2,%eax     #se 2 modalita dinamica
    je dinamica
    cmp $1,%eax     #se 1 modalita emergenza
    je emergenza
    jmp failure     #altrimenti safe mode



dinamica:
    movl $4,%eax        #stampa modalita
    movl $1,%ebx
    leal din, %ecx
    movl din_l,%edx
    int $0x80
    call dinamic        
    jmp exit

emergenza:

    movl $4,%eax        #stampa modalita
    movl $1,%ebx
    leal em, %ecx
    movl em_l,%edx
    int $0x80
  
    jmp exit

failure:

    movl $4,%eax        #stampa modalita
    movl $1,%ebx
    leal fail, %ecx
    movl fail_l,%edx
    int $0x80

exit:                   #chiude il programma
    movl $1, %eax 
    xorl %ebx, %ebx 
    int $0x80 







