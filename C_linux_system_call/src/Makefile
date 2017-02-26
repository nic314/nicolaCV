CC:= gcc
LD:= gcc
CFLAGS:= -c -Wall -Iinclude
LDFLAGS:= -Llib
LDLIBS:= -Lsupport -lm

SRCS:= main.c
OBJS:= $(SRCS:.c=.o)

all:elaborato

elaborato: $(OBJS)
	@echo linking... $(LD) $(LDFLAGS) -o $@ $^ $(LDLIBS)
	@$(LD) $(LDFLAGS) -o $@ $^ $(LDLIBS)

clean:
	@echo elimino...
	@rm *.o elaborato 

%.o: %.c
	@echo compilo... $(CC) $(CFLAGS) -o $@ $<
	@$(CC) $(CFLAGS) -o $@ $<


.PHONY: all clean





