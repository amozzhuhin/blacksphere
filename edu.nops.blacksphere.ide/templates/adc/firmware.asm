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

DEC_BUTTON_MASK equ 00000001b            ; Маска для выделения кнопки "-1"
INC_BUTTON_MASK equ 00000010b            ; Маска для выделения кнопки "+1"

; Сегмент стека
segment stack nobits start=STACK_START
        resb STACK_SIZE
stack_bottom:

; Сегмент данных
segment data nobits start=DATA_START
; Байт для храния текущего числа с АЦП
  adc_data resb 1

; Сегмент кода
segment code start=CODE_START

; Таблица кодов шестнацатиричных символов
hex_img_table:
         db 0x3f ;0
         db 0x06 ;1
         db 0x5b ;2
         db 0x4f ;3
         db 0x66 ;4
         db 0x6d ;5
         db 0x7d ;6
         db 0x07 ;7
         db 0x7f ;8
         db 0x6f ;9
         db 0x77 ;A
         db 0x7c ;b
         db 0x39 ;C
         db 0x5e ;d
         db 0x79 ;E
         db 0x71 ;F

; Процедура считывания состояния АЦП
InputADC:
         mov    al,0                    ; Запускаем преобразование импульсом \_/
         out    DD1_ADDR,al             ; Преобразование начинается по фронту импульса
         mov    al,1
         out    DD1_ADDR,al
.wait:   in     al,DD3_ADDR             ; Ждём единичку на выходе Rdy АЦП - признак
         test   al,1                    ; завершения преобразования
         jz     .wait
         in     al,DD2_ADDR             ; Считываем из АЦП данные
         mov    [adc_data],al
         ret

; Вывод текущего значения с АЦП
OutputData:
         mov     bx,hex_img_table
         mov     al,[adc_data]
         and     al,0x0f
         xlat
         out     DD5_ADDR,al
         mov     al,[adc_data]
         mov     cl,4
         shr     al,cl
         xlat
         out     DD4_ADDR,al
         ret

; Макроуровень программы
Start:
         ; инициализация
         mov    ax,STACK_START
         mov    ss,ax
         mov    sp,stack_bottom
; Главный цикл программы
MainLoop:
         call   InputADC               ; Ввод с АЦП
         call   OutputData             ; Вывод текущего значения
         jmp    MainLoop

segment boot start=BOOT_START
; При загрузке процессора сюда будет
; передано управление
         jmp    Start
