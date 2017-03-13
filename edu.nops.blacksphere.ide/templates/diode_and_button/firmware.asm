; Главный файл программного обеспечения устройства

; Комманды процессоров 8088/8086
CPU 8086

; Подключение файла констант архитектуры устройства
%include "include/device.inc"

; начало сегмента стека
STACK_START equ 100h
; размер сегмента стека
STACK_SIZE  equ 10h
; начало сегмента данных
DATA_START  equ 200h
; начало сегмента кода
CODE_START  equ RAM_SIZE

; Сегмент стека
segment stack nobits start=STACK_START
        resb STACK_SIZE
stack_bottom:

; Сегмент данных
segment data nobits start=DATA_START

; Сегмент кода
segment code start=CODE_START

; Макроуровень программы
start:
         ; инициализация
         mov    ax,DATA_START
         mov    ds,ax
         mov    es,ax
         mov    ax,STACK_START
         mov    ss,ax
         mov    sp,stack_bottom
; Главный цикл программы
MainLoop:
         in     al,DD1_ADDR
         out    DD2_ADDR,al
         jmp    MainLoop

segment boot start=BOOT_START
; При загрузке процессора сюда будет
; передано управление
         jmp    start
 