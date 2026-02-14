package org.firstinspires.ftc.teamcode.MCAEaglesPrograms;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Autonomous(name = "Drive Forward Only", group = "MCA EAGLES Programs")
public class autoMoveForward extends LinearOpMode {

    private DcMotor frontLeft, frontRight, backLeft, backRight;

    private static final double DRIVE_POWER = 0.4;
    private static final long DRIVE_MS = 800;

    @Override
    public void runOpMode() throws InterruptedException {

        frontLeft  = hardwareMap.dcMotor.get("Front_Left");
        frontRight = hardwareMap.dcMotor.get("Front_Right");
        backLeft   = hardwareMap.dcMotor.get("Back_Left");
        backRight  = hardwareMap.dcMotor.get("Back_Right");

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();
        if (isStopRequested()) return;

        frontLeft.setPower(DRIVE_POWER);
        backLeft.setPower(DRIVE_POWER);
        frontRight.setPower(DRIVE_POWER);
        backRight.setPower(DRIVE_POWER);

        sleep(DRIVE_MS);

        frontLeft.setPower(0);
        backLeft.setPower(0);
        frontRight.setPower(0);
        backRight.setPower(0);
    }
}