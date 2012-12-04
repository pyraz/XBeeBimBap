package com.trevorwhitney.ioio.domain;

import org.jruby.Ruby;
import org.jruby.RubyObject;
import org.jruby.javasupport.util.RuntimeHelpers;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.javasupport.JavaUtil;
import org.jruby.RubyClass;


public class ReceivePacket16Response extends RubyObject  {
    private static final Ruby __ruby__ = Ruby.getGlobalRuntime();
    private static final RubyClass __metaclass__;

    static {
        String source = new StringBuilder("require 'XBeeResponse'\n" +
            "require 'java'\n" +
            "java_package 'com.trevorwhitney.ioio.domain'\n" +
            "\n" +
            "class ReceivePacket16Response < XBeeResponse\n" +
            "  attr_reader :source_addr, :rssi, :options, :data\n" +
            "  \n" +
            "  def initialize(payload, checksum)\n" +
            "    super(payload, checksum)\n" +
            "    parse_payload\n" +
            "    @api_id = 0x81\n" +
            "  end\n" +
            "\n" +
            "  def to_s\n" +
            "    message = @data.map {|x| x.chr }\n" +
            "    message.join\n" +
            "  end\n" +
            "\n" +
            "  private\n" +
            "\n" +
            "  def parse_payload\n" +
            "    @source_addr = @payload[0] * 256 + @payload[1]\n" +
            "    @rssi = @payload[2]\n" +
            "    @options = @payload[3]\n" +
            "    data_end = length - 1\n" +
            "    @data = @payload[4..data_end]\n" +
            "  end\n" +
            "\n" +
            "end").toString();
        __ruby__.executeScript(source, "lib/ReceivePacket16Response.rb");
        RubyClass metaclass = __ruby__.getClass("ReceivePacket16Response");
        metaclass.setRubyStaticAllocator(ReceivePacket16Response.class);
        if (metaclass == null) throw new NoClassDefFoundError("Could not load Ruby class: ReceivePacket16Response");
        __metaclass__ = metaclass;
    }

    /**
     * Standard Ruby object constructor, for construction-from-Ruby purposes.
     * Generally not for user consumption.
     *
     * @param ruby The JRuby instance this object will belong to
     * @param metaclass The RubyClass representing the Ruby class of this object
     */
    private ReceivePacket16Response(Ruby ruby, RubyClass metaclass) {
        super(ruby, metaclass);
    }

    /**
     * A static method used by JRuby for allocating instances of this object
     * from Ruby. Generally not for user comsumption.
     *
     * @param ruby The JRuby instance this object will belong to
     * @param metaclass The RubyClass representing the Ruby class of this object
     */
    public static IRubyObject __allocate__(Ruby ruby, RubyClass metaClass) {
        return new ReceivePacket16Response(ruby, metaClass);
    }

    
    public  ReceivePacket16Response(Object payload, Object checksum) {
        this(__ruby__, __metaclass__);
        IRubyObject ruby_payload = JavaUtil.convertJavaToRuby(__ruby__, payload);
        IRubyObject ruby_checksum = JavaUtil.convertJavaToRuby(__ruby__, checksum);
        RuntimeHelpers.invoke(__ruby__.getCurrentContext(), this, "initialize", ruby_payload, ruby_checksum);

    }

    
    public Object to_s() {

        IRubyObject ruby_result = RuntimeHelpers.invoke(__ruby__.getCurrentContext(), this, "to_s");
        return (Object)ruby_result.toJava(Object.class);

    }

    
    public Object parse_payload() {

        IRubyObject ruby_result = RuntimeHelpers.invoke(__ruby__.getCurrentContext(), this, "parse_payload");
        return (Object)ruby_result.toJava(Object.class);

    }

}
