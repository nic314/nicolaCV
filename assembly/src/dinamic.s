.section .data  #gestisce la modalita dinamica dopo la 
                #stampa dei risultato ritorna alla funzione chiamante

passeggeri_totali:
    .long 0
nA:
    .long 0
nB:
    .long 0
nC:
    .long 0
nD:
    .long 0
nE:
    .long 0
nF:
    .long 0

x:
    .long 0
y:
    .long 0
z:
    .long 0

bias1:
    .long 0
bias2:
    .long 0
bias3:
    .long 0
bias4:
    .long 0

domanda_passeggeri:
    .ascii "Inserire il numero totale dei passeggeri a bordo\n"
domanda_passeggeri_l:
    .long . - domanda_passeggeri

troppi_passeggeri:
    .ascii "Valore errato, passeggieri massimi 180, reinserisci il valore\n"
troppi_passeggeri_l:
    .long . - troppi_passeggeri
        

passeggeriABC:
    .ascii "Inserire il numero totale passeggeri per le file A, B, C\n"
passeggeriABC_l:
    .long . - passeggeriABC

passeggeriDEF:
    .ascii "Inserire il numero totale passeggeri per le file D, E, F\n"
passeggeriDEF_l:
    .long . - passeggeriDEF

s_errore_disposizione:
    .ascii "Somma totali file diverso da totale passeggeri\n"
s_errore_disposizione_l:
    .long . - s_errore_disposizione

s_bias1:
    .ascii "Bias flap 1: "
s_bias1_l:
    .long . - s_bias1

s_bias2:
    .ascii "Bias flap 2: "
s_bias2_l:
    .long . - s_bias2

s_bias3:
    .ascii "Bias flap 3: "
s_bias3_l:
    .long . - s_bias3

s_bias4:
    .ascii "Bias flap 4: "
s_bias4_l:
    .long . - s_bias4


.section .text

 .global dinamic
 .type dinamic, @function

dinamic:
    
    movl $4,%eax                    #chiedo i passeggeri totali
    movl $1,%ebx
    leal domanda_passeggeri, %ecx
    movl domanda_passeggeri_l,%edx
    int $0x80
    jmp passeggeri                  

errore_passeggeri:
    movl $4,%eax                    #stampo il messaggio d`errore
    movl $1,%ebx
    leal troppi_passeggeri, %ecx
    movl troppi_passeggeri_l,%edx
    int $0x80

passeggeri:                         #leggo da tastiera il num di passegeri
    call atoi
    cmp $181,%eax                   #se >181 
    jge errore_passeggeri           #richiedo il valore
    movl %eax,passeggeri_totali     #altrimenti salvo il risultato
    jmp disposizione

errore_disposizione:
    movl $4,%eax                    #stampa messaggio d`errore
    movl $1,%ebx
    leal s_errore_disposizione, %ecx
    movl s_errore_disposizione_l,%edx
    int $0x80


disposizione:
    movl $4,%eax                    #stampa la richiesta
    movl $1,%ebx
    leal passeggeriABC, %ecx
    movl passeggeriABC_l,%edx
    int $0x80
    call inputpasseggeri            #chiama la funzione
    movl %eax,nA                    #salva i valori nelle variabili
    movl %ebx,nB
    movl %ecx,nC

    movl $4,%eax                    #stampa la richiesta
    movl $1,%ebx                
    leal passeggeriDEF, %ecx
    movl passeggeriDEF_l,%edx
    int $0x80
    call inputpasseggeri
    movl %eax,nD                    #salva i valori nelle variabili
    movl %ebx,nE
    movl %ecx,nF

    movl $0,%eax                    #somma tutti i passeggeri
    addl nA,%eax                    #in eax
    addl nB,%eax
    addl nC,%eax
    addl nD,%eax
    addl nE,%eax
    addl nF,%eax

    cmp passeggeri_totali,%eax      #se i passeggeri corrispondono
    je calcolo_bias                 #continua
    jmp errore_disposizione         #altrimenti richiede passeggeri

calcolo_bias:
    movl nA,%eax                    #x=nA-nF
    movl nF,%ebx
    subl %ebx,%eax
    movl %eax,x

    movl nB,%eax                    #y=nB-nE
    movl nE,%ebx
    subl %ebx,%eax
    movl %eax,y

    movl nC,%eax                    #z=nC-nD
    movl nD,%ebx
    subl %ebx,%eax
    movl %eax,z

    movl x,%eax                     #bias1=x*k1+y*k2
    movl $3,%ebx
    mull %ebx
    movl %eax,bias1
    movl y,%eax
    movl $6,%ebx
    mull %ebx
    addl %eax,bias1
    
   

    movl y,%eax                     #bias2=y*k2+z*k3
    movl $6,%ebx
    mull %ebx                       
    movl %eax,bias2
    movl z,%eax
    movl $12,%ebx
    mull %ebx                   
    addl %eax,bias2

    movl y,%eax                     #bias3=-x*k2-y*k3
    subl y,%eax                     #inverto 
    subl y,%eax
    movl $6,%ebx
    mull %ebx
    movl %eax,bias3
    movl z,%eax                     #inverto
    subl z,%eax                        
    subl z,%eax
    movl $12,%ebx
    mull %ebx
    addl %eax,bias3

    movl x,%eax                     #bias4=-x*k1-y*k2
    subl x,%eax                     #inverto
    subl x,%eax
    movl $3,%ebx
    mull %ebx
    movl %eax,bias4
    movl y,%eax
    subl y,%eax                     #inverto
    subl y,%eax
    movl $6,%ebx
    mull %ebx
    addl %eax,bias4


    movl $4,%eax                    #stampo "bias1
    movl $1,%ebx
    leal s_bias1, %ecx
    movl s_bias1_l,%edx
    int $0x80
    movl bias1,%eax                 #preparo il valore in eax
    call stampabias                 #e chiamo la funzione di stampa
    
    movl $4,%eax                    #stampo "bias2:"
    movl $1,%ebx
    leal s_bias2, %ecx
    movl s_bias2_l,%edx
    int $0x80
    movl bias2,%eax
    call stampabias

    movl $4,%eax                    #stampo bias3
    movl $1,%ebx
    leal s_bias3, %ecx
    movl s_bias3_l,%edx
    int $0x80
    movl bias3,%eax
    call stampabias

    movl $4,%eax                    #stampo bias4
    movl $1,%ebx
    leal s_bias4, %ecx
    movl s_bias4_l,%edx
    int $0x80
    movl bias4,%eax
    call stampabias
    

    ret




    
    



    


    


    

