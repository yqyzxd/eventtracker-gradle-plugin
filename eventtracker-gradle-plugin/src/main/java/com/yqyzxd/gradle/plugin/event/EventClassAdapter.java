package com.yqyzxd.gradle.plugin.event;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;

/**
 * Copyright (C), 2015-2022, 杭州迈优文化创意有限公司
 * FileName: EventClassAdapter
 * Author: wind
 * Date: 2022/6/23 17:36
 * Description: 描述该类的作用
 * Path: 路径
 * History:
 * <author> <time> <version> <desc>
 */
public class EventClassAdapter extends ClassVisitor {
    private boolean mAbsClass = false;

    public EventClassAdapter(int api, ClassVisitor classVisitor) {
        super(api, classVisitor);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        mAbsClass = (access & Opcodes.ACC_ABSTRACT) > 0 || (access & Opcodes.ACC_INTERFACE) > 0;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        if (mAbsClass) {
            return super.visitMethod(access, name, descriptor, signature, exceptions);
        }else {
            MethodVisitor methodVisitor=cv.visitMethod(access, name, descriptor, signature, exceptions);
            return new EventMethodAdapter(api,methodVisitor,access,name,descriptor);
        }

    }

    private class EventMethodAdapter extends AdviceAdapter{
        private boolean mHit;
        private EventAnnotationVisitor mEventAnnotationVisitor;
        /**
         * Constructs a new {@link AdviceAdapter}.
         *
         * @param api           the ASM API version implemented by this visitor. Must be one of {@link
         *                      Opcodes#ASM4}, {@link Opcodes#ASM5}, {@link Opcodes#ASM6} or {@link Opcodes#ASM7}.
         * @param methodVisitor the method visitor to which this adapter delegates calls.
         * @param access        the method's access flags (see {@link Opcodes}).
         * @param name          the method's name.
         * @param descriptor    the method's descriptor (see {@link Type Type}).
         */
        protected EventMethodAdapter(int api, MethodVisitor methodVisitor, int access, String name, String descriptor) {
            super(api, methodVisitor, access, name, descriptor);
        }


        @Override
        public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
            mHit =  descriptor.equals("Lcom/yqyzxd/event/Event;");
            if (mHit){
                AnnotationVisitor annotationVisitor= super.visitAnnotation(descriptor, visible);
                mEventAnnotationVisitor=new EventAnnotationVisitor(api,annotationVisitor);
                return mEventAnnotationVisitor;
            }else {
               return super.visitAnnotation(descriptor, visible);
            }

        }


        @Override
        protected void onMethodExit(int opcode) {
            super.onMethodExit(opcode);
            if (mHit && mEventAnnotationVisitor!=null){

                mv.visitLdcInsn(mEventAnnotationVisitor.getValue());
                visitMethodInsn(INVOKESTATIC,"com/yqyzxd/event/MEvent","onEvent","(Ljava/lang/String;)V",false);
            }
        }
    }

    private class EventAnnotationVisitor extends AnnotationVisitor{

        private String mValue;
        public EventAnnotationVisitor(int api, AnnotationVisitor annotationVisitor) {
            super(api, annotationVisitor);
        }

        @Override
        public void visit(String name, Object value) {
            super.visit(name, value);
            if (value instanceof String) {
                mValue = (String) value;
            }
        }


        public String getValue() {
            return mValue;
        }
    }

}
