.section .data #funzione utilizzata per selezionare la modalita di funzionamento

return:
    .long 0

count:
    .long 0
.section .text

    .global mode
    .type mode, @function

mode:
    popl return     #salvo l-indirizzo di ritorno nella var return

    call parametri  #ritorna 2,1,0 in eax a seconda del input

    cmp $0,%eax     #se eax=0
    je error        #input errato
    cmp $2,%eax     #se eax=2 dinamico
    je din_em
    cmp $1,%eax     #se eax=1 emergenza
    je din_em

error:              
    addl $1,count   #aumenta il contatore di 1
    cmp $3,count    #se count=3 limite tentativi,esce
    je exit

    call inputconsole
    cmp $0,%eax     #controlla risultato
    je error
    cmp $2,%eax
    je din_em
    cmp $1,%eax
    je din_em

    

exit:
    movl $0,%eax    #ritorna 0 eax
    pushl return    #ripristina l-indirizzo di ritorno
    ret

din_em:
    pushl return    #ritorna 2 o 1 in eax
    ret             


