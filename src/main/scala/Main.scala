import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.ec2.model.{InstanceState, InstanceStateName, Instance}
import com.amazonaws.services.ec2.{AmazonEC2Client, AmazonEC2}
import collection.JavaConversions._
import net.liftweb.json.{NoTypeHints, Serialization}

object Main extends App {
  implicit val formats = Serialization.formats(NoTypeHints)
  print(Serialization.write(new EC2HostProvider().hosts))
}

class EC2HostProvider extends EC2 {
  def instanceResponse = ec2.describeInstances

  def tagValue(instance: Instance, key: String) = instance.getTags.find(_.getKey == key).map(_.getValue).getOrElse("")

  def hosts = {
    HostList(for {
      res <- instanceResponse.getReservations
      instance <- res.getInstances if instance.getState.getName == InstanceStateName.Running.toString
    } yield {
      DeployInfoHost(instance.getPublicDnsName, app = tagValue(instance, "App"), stage = tagValue(instance, "Stage"))
    })
  }
}

case class HostList(hosts: Seq[DeployInfoHost])

case class DeployInfoHost(
  hostname: String,
  app: String,
  group: String = "",
  stage: String
)


trait EC2 extends AWS {
  lazy val ec2 : AmazonEC2 = new AmazonEC2Client(credentials)
  ec2.setEndpoint("ec2.eu-west-1.amazonaws.com")
}

trait AWS {
  lazy val accessKey = Option(System.getenv.get("aws_access_key")).getOrElse{
    sys.error("Cannot authenticate, 'aws_access_key' must be set as an environment variable")
  }
  lazy val secretAccessKey = Option(System.getenv.get("aws_secret_access_key")).getOrElse{
    sys.error("Cannot authenticate, aws_secret_access_key' must be set as an environment variable")
  }
  lazy val credentials = new BasicAWSCredentials(accessKey, secretAccessKey)
}