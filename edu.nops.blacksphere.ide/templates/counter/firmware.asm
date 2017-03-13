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

; Количество повторных чтений при устранении дребезга
BOUNCE_COUNT equ 100
; Маска для выделения кнопки "-1"
DEC_BUTTON_MASK equ 00000001b
; Маска для выделения кнопки "+1"
INC_BUTTON_MASK equ 00000010b

; Сегмент стека
segment stack nobits start=STACK_START
        resb STACK_SIZE
stack_bottom:

; Сегмент данных
segment data nobits start=DATA_START
; Байт для храния текущего числа счёчика
  digits resb 1
; Битовая маска активности кнопок
  buttons resb 1
; Значение с порта кнопок
  btn_port resb 1

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

; Процедура инициализации
Init:
         xor    al,al
         mov    byte[digits],al        ; Начальное значение счётчика равно 0
         mov    byte[btn_port],al      ; Начальное значение счётчика равно 0
         ret

; Процедура считывания состояния кнопок
; Определяется изменение состояния кнопок
InputButtons:
.input:  in     al,DD1_ADDR            ; Ввод с порта кнопок
         mov    ah,al
         mov    cx,BOUNCE_COUNT
.bounce: in     al,DD1_ADDR            ; Гашение дребезга
         cmp    al,ah                  ; Если прочитали другое значение
         jne    .input                 ; Повторно считываем
         loop   .bounce                ; Пока не прочитаем необходимое число раз
         mov    dl,[btn_port]
         mov    [btn_port],al
         xor    dl,al                  ; Определение переднего фронта
         and    dl,al                  ; нажатия на кнопки
         mov    [buttons],dl
         ret

; Обработка кнопок
ProcessButtons:
         mov    al,[buttons]
         test   al,DEC_BUTTON_MASK
         jz     .checkInc
         dec    byte[digits]
         jmp    .exit
.checkInc:
         test   al,INC_BUTTON_MASK
         jz     .exit
         inc    byte[digits]
.exit:   ret

OutputCounter:
         mov    bx,hex_img_table
         mov    al,[digits]
         and    al,0x0f
         xlat
         out    DD3_ADDR,al
         mov    al,[digits]
         mov    cl,4
         shr    al,cl
         xlat
         out    DD2_ADDR,al
         ret

; Макроуровень программы
Start:
         ; инициализация
         mov    ax,STACK_START
         mov    ss,ax
         mov    sp,stack_bottom
         call   Init                   ; Инициализация данных
; Главный цикл программы
MainLoop:
         call   InputButtons           ; Ввод с кнопок
         call   ProcessButtons         ; Обработка ввода с кнопок
         call   OutputCounter          ; Вывод текущего значения счётчика
         jmp    MainLoop

segment boot start=BOOT_START
; При загрузке процессора сюда будет
; передано управление
         jmp    Start
