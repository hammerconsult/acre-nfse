VAGRANTFILE_API_VERSION = "2"

Vagrant.configure(VAGRANTFILE_API_VERSION) do |config|

	#Base Box
	config.vm.box = "ubuntu/xenial64"

    config.vm.provider "virtualbox" do |vb|
        vb.customize ["modifyvm", :id, "--nictype1", "Am79C973"]
        vb.customize ["modifyvm", :id, "--nictype2", "Am79C973"]
    end

	config.vm.define "nfseserver" do |db|
	    db.vm.network "private_network", ip: "192.168.33.11"
	    db.vm.network "forwarded_port", guest: 5432, host: 5432
	    db.vm.host_name = "nfseserver.local"
	    db.vm.provision :shell do |shell|
	        shell.inline = "sudo locale-gen pt_BR;
	                        sudo locale-gen pt_BR.UTF-8;
	                        sudo update-locale;
	                        sudo mv /etc/localtime /etc/localtime-old;
	                        sudo ln -sf /usr/share/zoneinfo/America/Sao_Paulo /etc/localtime"
	    end
	    db.vm.provision :shell, :path => "bootstrapVagrant.sh"
	end
end
