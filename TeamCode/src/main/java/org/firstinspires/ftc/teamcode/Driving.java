package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This is a simple teleop routine for testing localization. Drive the robot around like a normal
 * teleop routine and make sure the robot's estimated pose matches the robot's actual pose (slight
 * errors are not out of the ordinary, especially with sudden drive motions). The goal of this
 * exercise is to ascertain whether the localizer has been configured properly (note: the pure
 * encoder localizer heading may be significantly off if the track width has not been tuned).
 */
@TeleOp(group = "drive")
public class Driving extends OpMode {

    ElapsedTime runtime = new ElapsedTime();

    DcMotorEx left = null;
    DcMotorEx right = null;
    DcMotorEx plate = null;
    DcMotorEx sling = null;
    DcMotorEx hook1 = null;
    Servo front = null;

    @Override
    public void init() {
        left = hardwareMap.get(DcMotorEx.class, "left");
        right = hardwareMap.get(DcMotorEx.class, "right");
        plate = hardwareMap.get(DcMotorEx.class, "plate");
        sling = hardwareMap.get(DcMotorEx.class, "sling");
        hook1 = hardwareMap.get(DcMotorEx.class, "hook1");
        front = hardwareMap.get(Servo.class, "servo");

        left.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        right.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        plate.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        sling.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        hook1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        plate.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        sling.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hook1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        left.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        right.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        sling.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        hook1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        right.setDirection(DcMotorSimple.Direction.REVERSE);
        front.setDirection(Servo.Direction.REVERSE);
        hook1.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void start() {
        front.setPosition(0);

        runtime.reset();
    }

    @Override
    public void loop() {
        float sp = gamepad1.right_bumper ? 0.4f : 1f;
        float ms = gamepad1.left_bumper ? 1f : 0.8f;

        float leftPower = (-gamepad1.left_trigger + gamepad1.right_trigger - gamepad1.left_stick_y) * sp * ms;
        float rightPower = (gamepad1.left_trigger - gamepad1.right_trigger - gamepad1.left_stick_y) * sp * ms;

        //float leftPower = (gamepad1.left_stick_x - gamepad1.left_stick_y) * sp * ms;
        //float rightPower = (-gamepad1.left_stick_x - gamepad1.left_stick_y) * sp * ms;

        debugTelemetry();

        if (gamepad1.a && !gamepad1.share) upPlate();
        else downPlate();

        left.setPower(leftPower);
        right.setPower(rightPower);

        /*
        if(gamepad1.share) {
            if (gamepad1.y)
                sling.setPower(1);
            else if (gamepad1.a)
                sling.setPower(-1);
            else
                sling.setPower(0);

            if (gamepad1.x) {
                hook1.setPower(1);
            } else if (gamepad1.b) {
                hook1.setPower(-1);
            } else {
                hook1.setPower(0);
            }
        } else{
            if(gamepad1.y) {
                sling.setPower(1f);
                hook1.setPower(1f);
            } else if(gamepad1.x) {
                sling.setPower(-1f);
                hook1.setPower(-1f);
            } else {
                sling.setPower(0);
                hook1.setPower(0);
            }
        } */// inainte sa fie cu pozitie

        //carlig sus 33005 25009
        //lift sus 20162 20733

        //switch lift 18479 carlig 21823

        if(gamepad1.y){
            sling.setTargetPosition(20733);
            hook1.setTargetPosition(25009);
            sling.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            hook1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            sling.setPower((sling.getCurrentPosition() < 18479) ? .95f : .92f);
            hook1.setPower(1);
        } else if(gamepad1.x){
            sling.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            hook1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            sling.setPower((sling.getCurrentPosition() >= 18479) ? -.92f : .95f);
            hook1.setPower(-1f);
        } else if(sling.getMode() == DcMotor.RunMode.RUN_WITHOUT_ENCODER){
            sling.setPower(0);
            hook1.setPower(0);
        }


    }

    boolean waitingLift = false, lifted = false;

    private void upPlate() {
        front.setPosition(0.15);
        if (!waitingLift && !lifted) {
            waitingLift = true;
            runtime.reset();
        }
        if (waitingLift && runtime.milliseconds() >= 300 && !lifted)
            waitingLift = false;
        if (!waitingLift) {
            lifted = true;
            plate.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            if (plate.getCurrentPosition() < 70)
                plate.setPower(0.5);
            else plate.setPower(0);
        }
    }

    private void downPlate() {
        lifted = false;
        front.setPosition(0);

        plate.setTargetPosition(0);
        plate.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        plate.setPower(1);
    }

    private void debugTelemetry() {
        telemetry.addData("roata stanga", left.getCurrentPosition());
        telemetry.addData("roata dreapta", right.getCurrentPosition());
        telemetry.addData("xleftstick", gamepad1.left_stick_x);
        telemetry.addData("yleftstick", gamepad1.left_stick_y);
        telemetry.addData("platepos", plate.getCurrentPosition());
        telemetry.addData("front", front.getPosition());
        telemetry.addData("lift", sling.getCurrentPosition());
        telemetry.addData("carlig", hook1.getCurrentPosition());
        telemetry.addData("share", gamepad1.share);
        telemetry.update();
    }

}
