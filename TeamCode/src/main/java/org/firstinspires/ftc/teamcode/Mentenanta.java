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
public class Mentenanta extends OpMode {

    ElapsedTime runtime = new ElapsedTime();

    DcMotorEx left = null;
    DcMotorEx right = null;
    DcMotorEx plate = null;
    DcMotorEx lift = null;
    DcMotorEx hook = null;
    Servo front = null;

    @Override
    public void init() {
        left = hardwareMap.get(DcMotorEx.class, "left");
        right = hardwareMap.get(DcMotorEx.class, "right");
        plate = hardwareMap.get(DcMotorEx.class, "plate");
        lift = hardwareMap.get(DcMotorEx.class, "lift");
        hook = hardwareMap.get(DcMotorEx.class, "hook");
        front = hardwareMap.get(Servo.class, "servo");

        left.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        right.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        plate.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        hook.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        plate.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hook.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        left.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        right.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        hook.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        plate.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        right.setDirection(DcMotorSimple.Direction.REVERSE);
        front.setDirection(Servo.Direction.REVERSE);
        hook.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void start() {
        front.setPosition(0);

        runtime.reset();
    }

    @Override
    public void loop() {
        plate.setPower(gamepad1.left_stick_y * .1f);
        lift.setPower(-gamepad2.left_stick_y);
        hook.setPower(-gamepad2.right_stick_y);
    }


}
