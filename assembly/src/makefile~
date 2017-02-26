
run:run.o dinamic.o atoi.o inputconsole.o inputpasseggeri.o itoa.o mode.o stampabias.o parametri.o
	ld run.o parametri.o mode.o inputconsole.o dinamic.o atoi.o inputpasseggeri.o itoa.o stampabias.o -o run -melf_i386

run.o:run.s
	as -gstabs -o run.o run.s --32

dinamic.o:dinamic.s
	as -gstabs -o dinamic.o dinamic.s --32

atoi.o:atoi.s
	as -gstabs -o atoi.o atoi.s --32

inputconsole.o:inputconsole.s
	as -gstabs -o inputconsole.o inputconsole.s --32

inputpasseggeri.o:inputpasseggeri.s
	as -gstabs -o inputpasseggeri.o inputpasseggeri.s --32

itoa.o:itoa.s
	as -gstabs -o itoa.o itoa.s --32

mode.o:mode.s
	as -gstabs -o mode.o mode.s --32

stampabias.o:stampabias.s
	as -gstabs -o stampabias.o stampabias.s --32

parametri.o:parametri.s
	as -gstabs -o parametri.o parametri.s --32

clean:
	rm -f *.o core run
