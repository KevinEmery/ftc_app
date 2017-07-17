package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robot.TestRobotWithEncoders;

/**
 * Created by kevineme on 6/27/17.
 */

@TeleOp(name="My First OpMode", group="Teleop")
public class TestOpMode extends LinearOpMode {

    private TestRobotWithEncoders robot = new TestRobotWithEncoders();

    @Override
    public void runOpMode() throws InterruptedException {

        robot.init(hardwareMap);

        waitForStart();

        driveUsingEncoders(TestRobotWithEncoders.TURN_SPEED, robot.leftMotor, robot.rightMotor, 2, -2, 4);
    }

    private void driveUsingEncoders(double speed,
                                   DcMotor leftMotor, DcMotor rightMotor,
                                   double leftInches, double rightInches,
                                   double timeoutInSeconds) {

        ElapsedTime runtime = new ElapsedTime();


        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            DcMotor.RunMode leftMotorRunMode = leftMotor.getMode();
            DcMotor.RunMode rightMotorRunMode = rightMotor.getMode();

            // Determine new target position, and pass to motor controller
            int newLeftTarget = leftMotor.getCurrentPosition() + (int)(leftInches * TestRobotWithEncoders.COUNTS_PER_INCH);
            int newRightTarget = rightMotor.getCurrentPosition() + (int)(rightInches * TestRobotWithEncoders.COUNTS_PER_INCH);
            leftMotor.setTargetPosition(newLeftTarget);
            rightMotor.setTargetPosition(newRightTarget);

            // Turn On RUN_TO_POSITION
            leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            leftMotor.setPower(Math.abs(speed));
            rightMotor.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutInSeconds) &&
                    (leftMotor.isBusy() && rightMotor.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
                telemetry.addData("Path2",  "Running at %7d :%7d", leftMotor.getCurrentPosition(), rightMotor.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            leftMotor.setPower(0);
            rightMotor.setPower(0);

            // Revert the run mode to old run mode that was in place prior to the method call
            leftMotor.setMode(leftMotorRunMode);
            rightMotor.setMode(rightMotorRunMode);
        }
    }
}
