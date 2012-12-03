require 'serialport'
require 'pry'

port_str = "/dev/ttyUSB0"
baud = 57600
data_bits = 8
stop_bits = 1
parity = SerialPort::NONE

puts "DB set up...connecting Serial"

sp = SerialPort.new(port_str, baud, data_bits, stop_bits, parity)

@value = 0
#binding.pry

while true do
  sp.each_byte do |b|
    puts b.to_s(16)
  end
end